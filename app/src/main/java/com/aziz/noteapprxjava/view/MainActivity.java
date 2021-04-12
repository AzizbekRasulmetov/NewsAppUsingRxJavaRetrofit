package com.aziz.noteapprxjava.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter adapter;
    private ArrayList<News.Article> articles;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();
        populateListWithData();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setClipToPadding(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));

        articles = new ArrayList<>();

        adapter = new RecyclerViewAdapter(this, articles);

        recyclerView.setAdapter(adapter);
    }

    private void populateListWithData() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        Single<News> observableSingle = apiService.fetchAllNews();
        observableSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<News>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(News news) {
                        Log.d(TAG, "onSuccess: ");
                        articles.addAll(news.getArticles());
                        Toast.makeText(MainActivity.this, "" + articles.size(), Toast.LENGTH_SHORT).show();

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}