package com.aziz.noteapprxjava.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aziz.noteapprxjava.R;
import com.aziz.noteapprxjava.model.News;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<News.Article> newsList;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewAdapter(Context context, List<News.Article> newsList, ClickListener listener) {
        this.newsList = newsList;
        this.context = context;
        clickListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, author, date;
        public ImageView image;

        public MyViewHolder(@NonNull View view) {
            super(view);

            //Hooks
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            author = view.findViewById(R.id.author);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        News.Article news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.description.setText(news.getDescription());
        holder.author.setText(news.getSource().getName());
        holder.date.setText(news.getPublishedAt().substring(0, news.getPublishedAt().indexOf("T")));

        Glide.with(context).load(news.getUrlToImage()).placeholder(R.drawable.load).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface ClickListener{
        void onItemClicked(int position);
    }


}
