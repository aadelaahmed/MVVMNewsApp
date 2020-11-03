package com.androiddevs.mvvmnewsapp.datasource;

import android.content.Intent;
import android.util.Log;

import androidx.paging.PagingSource;
import androidx.paging.rxjava2.RxPagingSource;

import com.androiddevs.mvvmnewsapp.data.NewsApi;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.model.NewsResponse;
import com.androiddevs.mvvmnewsapp.utils.Constants;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscription;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlePagingSource extends RxPagingSource<Integer, Article> {
    private static final String TAG = "ArticlePagingSource";
    LoadResult<Integer,Article> loadResult;
    Integer currentPageNumber;
    List<Article> listArticleResult;
    int pageNumber = 1;
    io.reactivex.disposables.CompositeDisposable compositeDisposable;
    String countryCode;
    NewsApi newsApi;
    boolean isError = false;

    public ArticlePagingSource( NewsApi newsApi, String countryCode) {
        //this.compositeDisposable = compositeDisposable;
        this.countryCode = countryCode;
        this.newsApi = newsApi;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Article>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        currentPageNumber = loadParams.getKey();
        Integer prevKey;
        if (currentPageNumber == null)
            currentPageNumber = pageNumber;
        if (currentPageNumber == 1)
            prevKey = null;
        else
            prevKey = currentPageNumber - 1;
        //Log.d(TAG, "loadSingle:check on load params --> "+loadParams.toString());
        Single<NewsResponse> singleData = newsApi.getAllArticles(countryCode,currentPageNumber, Constants.API_KEY);
        //Log.d(TAG, "loadSingle:check on single article async data --> "+singleData.toString());

      return singleData
                .subscribeOn(Schedulers.io())
                .map((Function<NewsResponse, LoadResult<Integer, Article>>) newsResponse -> {
                    Log.d(TAG, "apply:map function "+newsResponse.toString());
                    return new LoadResult.Page<Integer, Article>(
                            newsResponse.getArticles(),
                            prevKey,
                            currentPageNumber + 1,
                            LoadResult.Page.COUNT_UNDEFINED,
                            LoadResult.Page.COUNT_UNDEFINED
                    );
                })
                .onErrorReturn(throwable -> {
                    Log.d(TAG, "apply:error function "+throwable.toString());
                    return new LoadResult.Error<Integer,Article>(throwable);
                });
    }
}
