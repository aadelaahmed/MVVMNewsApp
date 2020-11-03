package com.androiddevs.mvvmnewsapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsActivity;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsViewModel;
import com.androiddevs.mvvmnewsapp.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class ArticleFragment extends Fragment implements View.OnClickListener {
    FragmentArticleBinding binding;
    NewsViewModel articleViewModel;
    Article article;
    WebView webView;
    FloatingActionButton saveArticleBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArticleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        webView = binding.webView;
        saveArticleBtn = binding.fab;
        saveArticleBtn.setOnClickListener(this);
        binding.paginationProgressBar.setVisibility(View.VISIBLE);
        article = ArticleFragmentArgs.fromBundle(getArguments()).getArticleArgument();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsActivity newsActivity = (NewsActivity) getActivity();
        articleViewModel = newsActivity.newsViewModel;
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(article.getUrl());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.paginationProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab:
                articleViewModel.insertArticleToDb(article);
                Snackbar.make(view,"Article Saved Successfully",Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
