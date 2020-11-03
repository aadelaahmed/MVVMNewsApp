package com.androiddevs.mvvmnewsapp.data;

import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.model.NewsResponse;
import com.androiddevs.mvvmnewsapp.utils.Constants;
import com.androiddevs.mvvmnewsapp.utils.Resource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("v2/top-headlines")
    io.reactivex.Single<NewsResponse> getAllArticles(
            @Query("country") String country,
            @Query("page") int pageNum,
            @Query("apiKey") String apiKey
    );

//    @GET("v2/top-headlines/us/{page}/")
//    Single<NewsResponse> getAllArticles(
//            @Query("country") String country,
//            @Query("page") int pageNum,
//            @Query("apiKey") String apiKey
//    );

    //search method or fetching all articles
    @GET("v2/everything")
    Single<NewsResponse> searchForNews(
            @Query("q") String searchQuery,
            @Query("page") int pageNum,
            @Query("apiKey") String apiKey
    );
}
