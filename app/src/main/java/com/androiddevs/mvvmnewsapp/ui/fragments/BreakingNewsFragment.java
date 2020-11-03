package com.androiddevs.mvvmnewsapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.adapter.LoadStateAdapter;
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter;
import com.androiddevs.mvvmnewsapp.databinding.FragmentBreakingNewsBinding;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsActivity;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsViewModel;
import com.androiddevs.mvvmnewsapp.utils.Constants;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BreakingNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "BreakingNewsFragment";
    FragmentBreakingNewsBinding binding;
    NewsViewModel breakingNewsViewModel;
    RecyclerView breakingNewsRv;
    NewsAdapter adapter;
    ProgressBar breakingNewsBar;
    SwipeRefreshLayout swipeRefreshLayout;
    NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBreakingNewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsActivity newsActivity = (NewsActivity) getActivity();
        breakingNewsViewModel = newsActivity.newsViewModel;
        navController = Navigation.findNavController(view);
        //breakingNewsBar = binding.paginationProgressBar;
        //0swipeRefreshLayout = binding.swipeRefresh;
//        ِActionBar actionBar = ((NewsActivity) getActivity()).getSupportActionBar();
//        actionBar.setTitle("Breaking News");
        iniUI();
        setLoadStateListener();
        breakingNewsViewModel.fetchPagedArticles("us")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PagingData<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PagingData<Article> articlePagingData) {
                        adapter.submitData(getLifecycle(), articlePagingData);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                breakingNewsViewModel.getLiveNetworkState().observe(this, new Observer<NetworkState>() {
//            @Override
//            public void onChanged(NetworkState networkState) {
//                switch (networkState.getStatus())
//                {
//                    case LOADING:
//                        swipeRefreshLayout.setRefreshing(true);
//                        showProgressBar();
//                        break;
//                    case LOADED:
//                    case ERROR:
//                    case ENDOFLIST:
//                        hideProgressBar();
//                        break;
//                }
//            }
//        });
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                breakingNewsViewModel.invalidateDataSource();
//            }
//        });
//        breakingNewsViewModel.getBreakingNews("us").observe(getActivity(), new Observer<Resource<NewsResponse>>() {
//            @Override
//            public void onChanged(Resource<NewsResponse> newsResponseResource) {
//                switch (newsResponseResource.getStatus())
//                {
//                    case RUNNING:
//                        showProgressBar();
//                        break;
//                    case FAILED:
//                        hideProgressBar();
//                        break;
//                    case SUCCESS:
//                    {
//                        NewsResponse temp = newsResponseResource.getData();
//                        adapter.differ.submitList(temp.getArticles());
//                        hideProgressBar();
//                        break;
//                    }
//                }
//            }
//        });
    }

    private void setLoadStateListener() {
        adapter.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
            @Override
            public Unit invoke(CombinedLoadStates combinedLoadStates) {
                if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.NotLoading) {
                    binding.rvBreakingNews.setVisibility(View.VISIBLE);
                    binding.emptyTv.setVisibility(View.GONE);
                    binding.paginationProgressBar.setVisibility(View.GONE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.retrySearchBtn.setVisibility(View.GONE);

                } else if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.Loading) {
                    binding.rvBreakingNews.setVisibility(View.GONE);
                    binding.emptyTv.setVisibility(View.GONE);
                    binding.paginationProgressBar.setVisibility(View.VISIBLE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.retrySearchBtn.setVisibility(View.GONE);

                } else if (combinedLoadStates.getSource().getRefresh() instanceof LoadState.Error) {
                    binding.rvBreakingNews.setVisibility(View.GONE);
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

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }



    private void iniUI() {
        breakingNewsRv = binding.rvBreakingNews;
        adapter = new NewsAdapter(getActivity(), this);
        breakingNewsRv.setAdapter(adapter);
        breakingNewsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.withLoadStateHeaderAndFooter(
                new LoadStateAdapter(this),
                new LoadStateAdapter(this)
        );
        breakingNewsRv.setHasFixedSize(true);
        binding.retrySearchBtn.setOnClickListener(this);
    }


    @Override
    public void onItemClickListener(Article article) {
        //using safe args to pass data
        BreakingNewsFragmentDirections.ActionBreakingNewsFragmentToArticleFragment action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article);
        navController.navigate(action);

        //using using bundle to pass data
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.ARTICLE_ARGS_KEY, article);
//        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
//        navController.navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle);
    }


    @Override
    public void onClick(View view) {
        adapter.retry();
    }
}

