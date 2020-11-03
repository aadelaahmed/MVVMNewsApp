package com.androiddevs.mvvmnewsapp.di;

import android.app.Application;
import android.app.SearchManager;
import android.content.Context;

import androidx.room.Room;

import com.androiddevs.mvvmnewsapp.data.NewsApi;
import com.androiddevs.mvvmnewsapp.db.ArticleDao;
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase;
import com.androiddevs.mvvmnewsapp.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NewsModule {

    @Provides
    @Singleton
    public static HttpLoggingInterceptor proviesHttpLogging()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
    @Provides
    @Singleton
    public static OkHttpClient provideHttpClient(HttpLoggingInterceptor interceptor)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        return client;
    }


    @Provides
    @Singleton
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public static NewsApi provideNewsApi(Retrofit retrofit)
    {
        return retrofit.create(NewsApi.class);
    }

    @Provides
    @Singleton
    public static CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    public static ArticleDatabase provideArticleDatabase(Application application)
    {
        return Room.databaseBuilder(
                application,
                ArticleDatabase.class,
                Constants.DATABASE_NAME
        )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static ArticleDao provideArticleDao(ArticleDatabase db)
    {
        return db.getArticleDao();
    }

//    @Provides
//    @Singleton
//    public static SearchManager provideSearchManager(Application application)
//    {
//        return (SearchManager) application.getSystemService(Context.SEARCH_SERVICE);
//    }
}
