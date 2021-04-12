package com.aziz.noteapprxjava.network;

import com.aziz.noteapprxjava.model.News;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

//    @FormUrlEncoded
//    @POST("notes/user/register")
//    Single<User> register(@Field("device_id") String deviceId);
//
//    @FormUrlEncoded
//    @POST("notes/new")
//    Single<Note> createNote(@Field("note") String note);

    @GET("top-headlines?country=us&category=sports&apiKey=80dc96dfa1464d139cfe988db417452b")
    Single<News> fetchAllNews();

//    @FormUrlEncoded
//    @PUT("notes/{id}")
//    Completable updateNote(@Path("id") int noteId, @Field("note") String note);
//
//    @DELETE("notes{id}")
//    Completable deleteNote(@Path("id") int noteId);

}
