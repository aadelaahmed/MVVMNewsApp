package com.androiddevs.mvvmnewsapp.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter;
import com.androiddevs.mvvmnewsapp.databinding.FragmentSavedNewsBinding;
import com.androiddevs.mvvmnewsapp.databinding.FragmentSearchNewsBinding;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.model.NewsResponse;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsActivity;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsViewModel;
import com.androiddevs.mvvmnewsapp.utils.Constants;
import com.androiddevs.mvvmnewsapp.utils.Resource;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SearchNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener, View.OnClickListener {
    FragmentSearchNewsBinding binding;
    NewsViewModel searchNewsViewModel;
    RecyclerView breakingNewsRv;
    NewsAdapter adapter;
    ProgressBar searchNewsBar;
    //EditText searchEdt;
    NavController navController;
    //Button searchRetryBtn;
    private static final String TAG = "SearchNewsFragment";
    @Inject
    SearchManager searchManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchNewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsActivity newsActivity = (NewsActivity) getActivity();
        searchNewsViewModel = newsActivity.newsViewModel;
        navController = Navigation.findNavController(view);
        //searchEdt = binding.etSearch;
        searchNewsBar = binding.paginationProgressBar;
        //searchRetryBtn = binding.searchRetryBtn;
        binding.retrySearchBtn.setOnClickListener(this);
//        ActionBar actionBar = ((NewsActivity) getActivity()).getSupportActionBar();
//        actionBar.setTitle("Search News");
        iniUI();
        setHasOptionsMenu(true);
        //onSearchTextChange();
    }

//    private void onSearchTextChange() {
//        Observable.create((ObservableOnSubscribe<String>) emitter -> searchEdt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                emitter.onNext(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        })).debounce(2, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .distinctUntilChanged()
//                .subscribe(new io.reactivex.rxjava3.core.Observer<String>() {
//                    @Override
//                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
//                        if (s != null)
//                        {
//                            breakingNewsRv.scrollToPosition(0);
//                            searchEdt.clearFocus();
//                            searchNewsViewModel.setNewsQuerySearch(s);
//                        }
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                        Log.d(TAG, "onError: error on first observable " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    private void searchOnText() {
        LiveData<PagingData<Article>> liveData = searchNewsViewModel.fetchPagedSearchArticles();
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<Article>>() {
            @Override
            public void onChanged(PagingData<Article> articlePagingData) {
                adapter.submitData(getLifecycle(), articlePagingData);
            }
        });
    }


    private void setLoadStateListener() {
        adapter.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
            @Override
            public Unit invoke(CombinedLoadStates combinedLoadStates) {
                if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.NotLoading) {
                    binding.rvSearchNews.setVisibility(View.VISIBLE);
                    binding.emptyTv.setVisibility(View.GONE);
                    binding.paginationProgressBar.setVisibility(View.GONE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.retrySearchBtn.setVisibility(View.GONE);

                } else if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.Loading) {
                    binding.rvSearchNews.setVisibility(View.GONE);
                    binding.emptyTv.setVisibility(View.GONE);
                    binding.paginationProgressBar.setVisibility(View.VISIBLE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.retrySearchBtn.setVisibility(View.GONE);

                } else if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.Error) {
                    binding.rvSearchNews.setVisibility(View.GONE);
                    binding.emptyTv.setVisibility(View.GONE);
                    binding.paginationProgressBar.setVisibility(View.GONE);
                    binding.errorTv.setVisibility(View.VISIBLE);
                    binding.retrySearchBtn.setVisibility(View.VISIBLE);
                } else if (combinedLoadStates.getSource().getAppend() instanceof LoadState.Error
                        || combinedLoadStates.getSource().getPrepend() instanceof LoadState.Error
                        || combinedLoadStates.getAppend() instanceof LoadState.Error
                        || combinedLoadStates.getPrepend() instanceof LoadState.Error
                ) {
                    showMessage("Something went wrong");
                    binding.retrySearchBtn.setVisibility(View.VISIBLE);
                } else if (
                        combinedLoadStates.getSource().getRefresh() instanceof LoadState.NotLoading
                                && combinedLoadStates.getSource().getAppend().getEndOfPaginationReached()
                                && adapter.getItemCount() < 1
                ) {
                    binding.emptyTv.setVisibility(View.VISIBLE);
                    binding.paginationProgressBar.setVisibility(View.GONE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.retrySearchBtn.setVisibility(View.GONE);
                }
                return Unit.INSTANCE;
            }
        });
    }

    private void iniUI() {
        breakingNewsRv = binding.rvSearchNews;
        breakingNewsRv.setItemAnimator(null);
        adapter = new NewsAdapter(getActivity(), this);
        breakingNewsRv.setAdapter(adapter);
        breakingNewsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        breakingNewsRv.setHasFixedSize(true);
        setLoadStateListener();
        searchOnText();
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(Article article) {
        //using safe args to pass data
        SearchNewsFragmentDirections.ActionSearchNewsFragmentToArticleFragment action =
                SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article);
        navController.navigate(action);
        //using bundle to pass data
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.ARTICLE_ARGS_KEY, article);
//        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
//        navController.navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_search_item);
        searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    breakingNewsRv.scrollToPosition(0);
                    searchView.clearFocus();
                    //searchEdt.clearFocus();
                    searchNewsViewModel.setNewsQuerySearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Observable.create((ObservableOnSubscribe<String>) emitter ->
//                        emitter.onNext(newText)
//                ).debounce(2, TimeUnit.SECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .distinctUntilChanged()
//                        .subscribe(new io.reactivex.rxjava3.core.Observer<String>() {
//                            @Override
//                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
//                                if (s != null) {
//                                    breakingNewsRv.scrollToPosition(0);
//                                    searchView.clearFocus();
//                                    //searchEdt.clearFocus();
//                                    searchNewsViewModel.setNewsQuerySearch(s);
//                                }
//                            }
//
//                            @Override
//                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                                Log.d(TAG, "onError: error on first observable " + e.getMessage());
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//                return true;
                return false;
            }
        });
        //item.getIcon().setVisible(false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        adapter.retry();
    }
}
