package com.aziz.noteapprxjava.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.aziz.noteapprxjava.model.News;
import com.aziz.noteapprxjava.network.ApiClient;
import com.aziz.noteapprxjava.network.ApiService;
import com.aziz.noteapprxjava.utils.MyDividerItemDecoration;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BreakingNewsFragment extends Fragment implements RecyclerViewAdapter.ClickListener {

    public static String CATEGORY = "general";
    public static final String API_KEY = "80dc96dfa1464d139cfe988db417452b";

    private RecyclerViewAdapter adapter;
    private ArrayList<News.Article> articles;
    private Disposable disposable;
    private ProgressDialog progressDialog;

    public BreakingNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_breaking_news, container, false);
        buildRecyclerView(view);
        fillRecyclerViewWithNews();
        return view;
    }

    private void buildRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_breaking_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        articles = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getContext(), articles, this);
        recyclerView.setAdapter(adapter);
    }

    public void fillRecyclerViewWithNews(){
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Single<News> newsSingle = apiService.fetchNewsByCategories(CATEGORY.toLowerCase(), API_KEY);
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
                        progressDialog.dismiss();
                        articles.addAll(news.getArticles());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error occured while loading data.\n\nPossible causes:\nInternet connection\nRequest limit", Toast.LENGTH_SHORT).show();
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
        fragmentTransaction.replace(R.id.fragment_container, new DetailsFragment(articles.get(position)));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}