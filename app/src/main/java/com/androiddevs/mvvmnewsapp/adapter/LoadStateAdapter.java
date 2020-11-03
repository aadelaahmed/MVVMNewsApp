package com.androiddevs.mvvmnewsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddevs.mvvmnewsapp.R;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;

public class LoadStateAdapter extends androidx.paging.LoadStateAdapter<LoadStateViewHolder> {

    View.OnClickListener mRetryCallback;
    Unit retry;
    public LoadStateAdapter(View.OnClickListener retryCallback)
    {
        mRetryCallback = retryCallback;
        //this.retry = retry;
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder loadStateViewHolder, @NotNull LoadState loadState) {
        loadStateViewHolder.bind(loadState);
    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, @NotNull LoadState loadState) {
        return LoadStateViewHolder.create(viewGroup,mRetryCallback);
    }


}
