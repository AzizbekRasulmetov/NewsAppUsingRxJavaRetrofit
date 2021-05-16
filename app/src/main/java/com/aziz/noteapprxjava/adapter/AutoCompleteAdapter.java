package com.aziz.noteapprxjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aziz.noteapprxjava.R;
import com.aziz.noteapprxjava.model.News;
import com.aziz.noteapprxjava.network.ApiClient;
import com.aziz.noteapprxjava.network.ApiService;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AutoCompleteAdapter extends ArrayAdapter<News.Article> {

    private List<News.Article> newsList;
    private List<News.Article> filteredNewsList;

    private ApiService apiService;
    private Context context;

    public AutoCompleteAdapter(@NonNull Context context, List<News.Article> news) {
        super(context, 0, news);
        this.context = context;
        this.newsList.addAll(news);
        apiService = ApiClient.getClient(context).create(ApiService.class);
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            filteredNewsList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredNewsList.addAll(newsList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                Observable<List<News.Article>> observable = Observable.fromArray(newsList);
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<News.Article>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<News.Article> articles) {
                                for (News.Article a : articles) {
                                    if (a.getTitle().contains(filterPattern)) {
                                        filteredNewsList.add(a);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            filterResults.values = filteredNewsList;
            filterResults.count = filteredNewsList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((News.Article) resultValue).getTitle();
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        TextView title = convertView.findViewById(R.id.title);
        TextView desc = convertView.findViewById(R.id.description);
        TextView author = convertView.findViewById(R.id.author);
        TextView date = convertView.findViewById(R.id.date);

        News.Article article = getItem(position);

        if(article != null){
            title.setText(article.getTitle());
            desc.setText(article.getDescription());
            author.setText(article.getSource().getName());
            date.setText(article.getPublishedAt());
            Glide.with(context).load(article.getUrlToImage()).into(image);
        }
        return convertView;
    }
}
