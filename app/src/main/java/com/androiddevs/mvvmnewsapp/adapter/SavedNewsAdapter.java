package com.androiddevs.mvvmnewsapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddevs.mvvmnewsapp.databinding.ItemArticlePreviewBinding;
import com.androiddevs.mvvmnewsapp.databinding.LoadStateItemBinding;
import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> {
    List<Article> list = new ArrayList<>();
    Context mContext;
    public SavedNewsAdapter(Context mContext)
    {
        this.mContext = mContext;
    }

    public Article getItemAt(int position)
    {
        return list.get(position);
    }
    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticlePreviewBinding binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SavedNewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        Article article = list.get(holder.getBindingAdapterPosition());
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setNewList(List<Article> newList)
    {
        this.list = newList;
        notifyDataSetChanged();
    }

    public class SavedNewsViewHolder extends RecyclerView.ViewHolder{
        ImageView articleImage;
        TextView articleSource, articleTitle, articleDesc, articlePubAt,articleAuthor,articleTime;
        ProgressBar articleProgressBar;
        ItemArticlePreviewBinding binding;
        public SavedNewsViewHolder(ItemArticlePreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            articleTitle = binding.articleTitle;
            articleDesc = binding.articleDescription;
            articleImage = binding.articleImage;
            articleSource = binding.articleSource;
            articlePubAt = binding.articlePublishedAtTv;
            articleAuthor = binding.articleAuthor;
            articleTime = binding.articleTime;
            articleProgressBar = binding.articlePrograssLoadPhoto;
        }

        public void bind(Article currentArticle) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.error(Utils.getRandomDrawbleColor());
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.centerCrop();
            Glide.with(mContext)
                    .load(currentArticle.getUrlToImage())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            articleProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            articleProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(articleImage);
            articleTitle.setText(currentArticle.getTitle());
           articleDesc.setText(currentArticle.getDescription());
            articleSource.setText(currentArticle.getSource().getName());
            articleTime.setText(" \u2022 " + Utils.DateToTimeFormat(currentArticle.getPublishedAt()));
            articlePubAt.setText(Utils.DateFormat(currentArticle.getPublishedAt()));
            articleAuthor.setText(currentArticle.getAuthor());
        }
    }
}
