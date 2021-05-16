package com.aziz.noteapprxjava.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aziz.noteapprxjava.R;
import com.aziz.noteapprxjava.adapter.RecyclerViewAdapter;
import com.aziz.noteapprxjava.adapter.SavedAdapter;
import com.aziz.noteapprxjava.database.NewsDao;
import com.aziz.noteapprxjava.database.NewsDatabase;
import com.aziz.noteapprxjava.database.NewsEntity;
import com.aziz.noteapprxjava.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SavedNewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SavedAdapter adapter;
    private Disposable disposable;
    private List<NewsEntity> newsEntityList;

    public SavedNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_news, container, false);
        buildRecyclerView(view);
        loadDataFromDB();
        return view;
    }

    private void buildRecyclerView(View view){
        newsEntityList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view_saved);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));

        adapter = new SavedAdapter(getContext(), newsEntityList, null);
        recyclerView.setAdapter(adapter);
    }

    private void loadDataFromDB(){
        NewsDao newsDao = NewsDatabase.getInstance(getContext()).newsDao();
        newsDao.getAllNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<NewsEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<NewsEntity> newsEntities) {
                        if(newsEntities != null && newsEntities.size() > 0){
                            newsEntityList.addAll(newsEntities);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteAllNews(){
        NewsDao newsDao = NewsDatabase.getInstance(getContext()).newsDao();
        newsDao.deleteAllNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}