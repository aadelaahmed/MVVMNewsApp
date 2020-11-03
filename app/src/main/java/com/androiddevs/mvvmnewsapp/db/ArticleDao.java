package com.androiddevs.mvvmnewsapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.androiddevs.mvvmnewsapp.model.Article;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticleDB(Article article);

    @Query("Select * from articles")
    LiveData<List<Article>> fetchAllArticlesDB();

    @Delete
    void deleteArticleDB(Article article);
}
