package com.aziz.noteapprxjava.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.aziz.noteapprxjava.R;
import com.aziz.noteapprxjava.adapter.AutoCompleteAdapter;
import com.aziz.noteapprxjava.adapter.RecyclerViewAdapter;
import com.aziz.noteapprxjava.model.News;
import com.aziz.noteapprxjava.network.ApiClient;
import com.aziz.noteapprxjava.network.ApiService;
import com.aziz.noteapprxjava.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchNewsFragment extends Fragment implements RecyclerViewAdapter.ClickListener {

    private static final String TAG = "SearchNewsFragment";

    private RecyclerViewAdapter adapter;
    private List<News.Article> newsArrayList;
    private AutoCompleteTextView autoCompleteTextView;

    private Disposable disposable;

    public SearchNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_news, container, false);
        buildRecyclerView(view);
        autoCompleteTextView = view.findViewById(R.id.search_news_edit);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fillRecyclerViewWithData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void buildRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setClipToPadding(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));

        newsArrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getContext(), newsArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    public void fillRecyclerViewWithData(String text){
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Single<News> newsSingle = apiService.searchNews(text, "80dc96dfa1464d139cfe988db417452b");
        newsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<News>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(News news) {
                        newsArrayList.clear();
                        newsArrayList.addAll(news.getArticles());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onItemClicked(int position) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new DetailsFragment(newsArrayList.get(position)));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}