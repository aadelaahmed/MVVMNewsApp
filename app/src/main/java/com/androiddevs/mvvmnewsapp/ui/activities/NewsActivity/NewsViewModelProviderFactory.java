package com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.androiddevs.mvvmnewsapp.Repository.NewsRepository;

import io.reactivex.disposables.CompositeDisposable;
//extends ViewModelProvider.AndroidViewModelFactory
public class NewsViewModelProviderFactory  {
//    NewsRepository repository;
//    CompositeDisposable compositeDisposable;
//    Application application;
//    public NewsViewModelProviderFactory(@NonNull Application application, NewsRepository repository, io.reactivex.disposables.CompositeDisposable compositeDisposable) {
//        super(application);
//        this.application = application;
//        this.repository = repository;
//        this.compositeDisposable = compositeDisposable;
//    }
////    public NewsViewModelProviderFactory(NewsRepository repository, Application application)
////    {
////        this.repository = repository;
////        this.application = application;
//////        this.compositeDisposable= compositeDisposable;
////    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return (T) new NewsViewModel(application,compositeDisposable);
//    }
}
