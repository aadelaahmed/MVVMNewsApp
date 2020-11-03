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

public class LoadStateViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;
    TextView loadStateError;
    Button retryBtn;

    public LoadStateViewHolder(@NonNull View itemView, View.OnClickListener retryCallback) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.load_state_progress_bar);
        loadStateError = itemView.findViewById(R.id.load_state_error_tv);
        retryBtn = itemView.findViewById(R.id.load_state_retry_btn);
        retryBtn.setOnClickListener(retryCallback);
    }

    public void bind(LoadState loadState) {
        if (loadState instanceof LoadState.Error) {
            LoadState.Error tempLoadState = (LoadState.Error) loadState;
            loadStateError.setText(tempLoadState.getError().getLocalizedMessage());
            progressBar.setVisibility(View.GONE);
            retryBtn.setVisibility(View.VISIBLE);
            loadStateError.setVisibility(View.VISIBLE);
        } else if (loadState instanceof LoadState.Loading)
        {
            retryBtn.setVisibility(View.GONE);
            loadStateError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    public static LoadStateViewHolder create(@NotNull ViewGroup viewGroup, View.OnClickListener mRetryCallback)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_state_item,viewGroup,false);
        return new LoadStateViewHolder(view,mRetryCallback);
    }
}
