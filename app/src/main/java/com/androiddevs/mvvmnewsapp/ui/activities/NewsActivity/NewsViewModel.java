package com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity;

import android.app.Application;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.androiddevs.mvvmnewsapp.Repository.NewsRepository;
import com.androiddevs.mvvmnewsapp.data.NewsApi;
import com.androiddevs.mvvmnewsapp.db.ArticleDao;
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.utils.Constants;
import com.androiddevs.mvvmnewsapp.utils.Resource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import kotlinx.coroutines.CoroutineScope;

public class NewsViewModel extends AndroidViewModel {

    private static final String TAG = "NewsViewModel";
    @Inject
    NewsRepository repository;
    @Inject
    io.reactivex.disposables.CompositeDisposable compositeDisposable;
    public static Resource resource;
    LiveData<List<Article>> localLiveArticles;
    MutableLiveData<String> liveSearchQuery;
    @Inject
    SavedStateHandle stateHandle;
    //int pageNumber = 1;
//    DataSource<Integer, Article> mDataSource;
    //NewsApi newsApi;
//    MutableLiveData<PagingData<Article>> pagingLiveArticles = new MutableLiveData<>();
//    MutableLiveData<PagingData<Article>> pagingLiveSearch = new MutableLiveData<>();
//    Observable<PagingData<Article>> currentSearchResult;
//    String currentQueryText;

    @ViewModelInject
    public NewsViewModel(Application application, CompositeDisposable compositeDisposable, @Assisted SavedStateHandle savedStateHandle, NewsRepository repository, ArticleDao dao) {
        super(application);
        this.compositeDisposable = compositeDisposable;
        this.repository = repository;
        this.stateHandle = savedStateHandle;
        //this.db = ArticleDatabase.getInstance(application.getApplicationContext());
        //this.mDataSource = new ArticleDataSourceFactory().create();
//        this.newsApi = NewsClient.getInstance();
        //this.repository = new NewsRepository(this.db, compositeDisposable, newsApi);
    }

//    public LiveData<NetworkState> getLiveNetworkState()
//    {
//        return repository.getLiveNetworkState();
//    }
//
//    public LiveData<PagedList<Article>> getBreakingNews(String countryCode) {
//        liveArticlePagedList = repository.fetchBreakingNews(countryCode,pageNumber, Constants.BASE_URL);
//        return liveArticlePagedList;
//    }

    public Observable<PagingData<Article>> fetchPagedArticles(String countryCode) {
        Observable<PagingData<Article>> newPagedResult = repository.fetchLivePagedList(countryCode);
//        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
//        PagingRx.cachedIn(newPagedResult,viewModelScope);
        return newPagedResult;
    }


    public LiveData<PagingData<Article>> fetchPagedSearchArticles() {
        liveSearchQuery = stateHandle.getLiveData(Constants.SAVED_STATE_KEY,Constants.DEFAULT_SEARCH_QUERY);
        LiveData<PagingData<Article>> newSearchPagedResult = Transformations.switchMap(liveSearchQuery,
                newSearchQuery -> repository.fetchLiveSearchPagedList(newSearchQuery)
        );
//        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
//        PagingLiveData.cachedIn(newSearchPagedResult, viewModelScope);
        return newSearchPagedResult;
    }

    public void setNewsQuerySearch(String newsQuerySearch)
    {
        liveSearchQuery.setValue(newsQuerySearch);
    }

    public void insertArticleToDb(Article article) {
        repository.insertArticleToDb(article);
    }

    public LiveData<List<Article>> fetchArticleFromDb() {
        localLiveArticles = repository.fetchArticleFromDb();
        return localLiveArticles;
    }

    public void deleteArticleFromDb(Article article) {
        repository.deleteArticleFromDb(article);
    }

//    public void invalidateDataSource() {
//        mDataSource.invalidate();
//    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
