package com.androiddevs.mvvmnewsapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.adapter.NewsAdapter;
import com.androiddevs.mvvmnewsapp.adapter.SavedNewsAdapter;
import com.androiddevs.mvvmnewsapp.databinding.FragmentArticleBinding;
import com.androiddevs.mvvmnewsapp.databinding.FragmentSavedNewsBinding;
import com.androiddevs.mvvmnewsapp.databinding.ItemArticlePreviewBinding;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsActivity;
import com.androiddevs.mvvmnewsapp.ui.activities.NewsActivity.NewsViewModel;
import com.androiddevs.mvvmnewsapp.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SavedNewsFragment extends Fragment implements NewsAdapter.OnItemClickListener {
    FragmentSavedNewsBinding binding;
    Button btnNav;
    NewsViewModel saveNewsViewModel;
    RecyclerView savedNewsRV;
    ProgressBar progressBar;
    TextView emptyTextView;
    //    NewsAdapter adapter;
    SavedNewsAdapter savedNewsAdapter;
    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedNewsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsActivity newsActivity = (NewsActivity) getActivity();
        saveNewsViewModel = newsActivity.newsViewModel;
        progressBar = binding.progressBar;
        emptyTextView = binding.emptyTv;
        progressBar.setVisibility(View.VISIBLE);
        navController = Navigation.findNavController(view);
//        ActionBar actionBar = ((NewsActivity) getActivity()).getSupportActionBar();
//        actionBar.setTitle("Saved News");
        iniUI();
        fetchNewsData();
    }

    private void fetchNewsData() {
        //List<Article> newArticlesList = new ArrayList();
        saveNewsViewModel.fetchArticleFromDb().observe(getActivity(), new androidx.lifecycle.Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (!articles.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    savedNewsAdapter.setNewList(articles);
                } else {
                    progressBar.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }
//                adapter.submitData(getViewLifecycleOwner().getLifecycle(),articles);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                Article currentArticle = savedNewsAdapter.getItemAt(position);
                saveNewsViewModel.deleteArticleFromDb(currentArticle);
                Snackbar.make(getView(), "Aricle Deleted Successfully", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                saveNewsViewModel.insertArticleToDb(currentArticle);
                            }
                        }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(savedNewsRV);
    }

    @Override
    public void onItemClickListener(Article article) {
        //using safe args to pass data
        SavedNewsFragmentDirections.ActionSavedNewsFragmentToArticleFragment action =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article);
        navController.navigate(action);

        //using bundle to pass data
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.ARTICLE_ARGS_KEY, article);
//        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
//        navController.navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle);
    }

    private void iniUI() {
        savedNewsRV = binding.rvSavedNews;
        savedNewsAdapter = new SavedNewsAdapter(getActivity());
        savedNewsRV.setAdapter(savedNewsAdapter);
        savedNewsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedNewsRV.setHasFixedSize(true);
    }
}
