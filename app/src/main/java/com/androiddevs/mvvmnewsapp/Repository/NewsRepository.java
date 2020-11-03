package com.androiddevs.mvvmnewsapp.Repository;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.rxjava2.PagingRx;

import com.androiddevs.mvvmnewsapp.data.NewsApi;
import com.androiddevs.mvvmnewsapp.datasource.ArticlePagingSource;
import com.androiddevs.mvvmnewsapp.datasource.SearchPagingSource;
import com.androiddevs.mvvmnewsapp.db.ArticleDao;
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class NewsRepository {
    private static final String TAG = "NewsRepository";
    @Inject
    io.reactivex.disposables.CompositeDisposable compositeDisposable;
    //ArticleDataSourceFactory articleFactory;
    @Inject
    NewsApi newsApi;
    @Inject
    ArticleDao dao;
    @Inject
    public NewsRepository(ArticleDao dao, CompositeDisposable compositeDisposable, NewsApi newsApi) {
        this.compositeDisposable = compositeDisposable;
        this.newsApi = newsApi;
        this.dao = dao;
    }

    public LiveData<PagingData<Article>> fetchLiveSearchPagedList(String searchText) {
        PagingConfig config = new PagingConfig(Constants.ARTICLES_PER_PAGE);
        SearchPagingSource searchPagingSource = new SearchPagingSource(newsApi, searchText);
        Pager<Integer, Article> pager = new Pager(
                config,
                () -> searchPagingSource
        );
        LiveData<PagingData<Article>> liveSearchData = PagingLiveData.getLiveData(pager);
        return liveSearchData;
    }

    public Observable<PagingData<Article>> fetchLivePagedList(String countryCode)
    {
        Pager<Integer,Article> pager = new Pager(
                new PagingConfig(Constants.ARTICLES_PER_PAGE),
                () -> new ArticlePagingSource(newsApi,countryCode)
        );
       Observable<PagingData<Article>> observablePagedList =  PagingRx.getObservable(pager);
       return observablePagedList;
    }
//    public LiveData<NetworkState> getLiveNetworkState()
//    {
//        return Transformations.switchMap(articleFactory.getmArticleDataSource(),
//                new Function<ArticleDataSource, LiveData<NetworkState>>() {
//                    @Override
//                    public LiveData<NetworkState> apply(ArticleDataSource input) {
//                        return input.getmNetworkState();
//                    }
//                });
//    }
//
//    public LiveData<PagedList<Article>> fetchBreakingNews(String countryCode, int pageNum, String apiKey) {
//        articleFactory = new ArticleDataSourceFactory(compositeDisposable,countryCode);
//        PagedList.Config config =
//                new PagedList.Config
//                        .Builder()
//                        .setEnablePlaceholders(false)
//                        .setPageSize(Constants.ARTICLES_PER_PAGE)
//                        .build();
//        livePagedArticle = new LivePagedListBuilder(articleFactory,config).build();
//        return livePagedArticle;
//    }

//    public Single<NewsResponse> fetchBreakingNews(String countryCode, int pageNum, String apiKey)
//    {
//        return NewsClient.getInstance().getAllArticles(countryCode,pageNum,apiKey);
//    }

//    public Single<NewsResponse> fetchSearchNews(String queryText, int pageNum, String apiKey) {
//        //return NewsClient.getInstance().searchForNews(queryText, pageNum, apiKey);
//        return null;
//    }
//
    public void insertArticleToDb(Article article) {
        dao.insertArticleDB(article);
    }

    public LiveData<List<Article>> fetchArticleFromDb() {
        return dao.fetchAllArticlesDB();
    }

    public void deleteArticleFromDb(Article article) {
        dao.deleteArticleDB(article);
    }


}
