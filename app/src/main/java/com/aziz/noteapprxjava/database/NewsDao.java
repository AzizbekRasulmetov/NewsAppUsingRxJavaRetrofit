package com.aziz.noteapprxjava.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface NewsDao {

    @Insert
    Completable insert(NewsEntity newsEntity);

    @Delete
    Completable delete(NewsEntity newsEntity);

    @Query("DELETE FROM news_table")
    Completable deleteAllNews();

    @Query("SELECT * FROM news_table")
    Single<List<NewsEntity>> getAllNews();

}
