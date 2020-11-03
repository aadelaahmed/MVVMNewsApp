package com.androiddevs.mvvmnewsapp.datasource;

import androidx.paging.rxjava2.RxPagingSource;

import com.androiddevs.mvvmnewsapp.data.NewsApi;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.model.NewsResponse;
import com.androiddevs.mvvmnewsapp.utils.Constants;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchPagingSource extends RxPagingSource<Integer, Article> {
    String searchText;
    NewsApi newsApi;
    Integer currentPageNumber,prevKey;
    public SearchPagingSource(NewsApi newsApi, String searchText) {
        this.searchText = searchText;
        this.newsApi = newsApi;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Article>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        currentPageNumber = loadParams.getKey();
        if (currentPageNumber == null)
            currentPageNumber = 1;
        if (currentPageNumber == 1)
            prevKey = null;
        else
            prevKey = currentPageNumber - 1;

        Single<NewsResponse> single = newsApi.searchForNews(searchText,currentPageNumber, Constants.API_KEY);
        return single
                .subscribeOn(Schedulers.io())
                .map((Function<NewsResponse, LoadResult<Integer, Article>>) newsResponse -> new LoadResult.Page<>(
                        newsResponse.getArticles(),
                        prevKey,
                        currentPageNumber + 1,
                        LoadResult.Page.COUNT_UNDEFINED,
                        LoadResult.Page.COUNT_UNDEFINED

                ))
                .onErrorReturn(throwable -> new LoadResult.Error(throwable));
    }
}
