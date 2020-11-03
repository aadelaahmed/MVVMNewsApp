package com.androiddevs.mvvmnewsapp.adapter;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.Lifecycle;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.AsyncPagingDataDiffer;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.androiddevs.mvvmnewsapp.R;
import com.androiddevs.mvvmnewsapp.databinding.ItemArticlePreviewBinding;
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

import java.util.List;

public class NewsAdapter extends PagingDataAdapter<Article, NewsAdapter.NewsViewHolder> {
    Context mContext;
    OnItemClickListener onItemClickListener;
    //PagedList<Article> pagedList = getCurrentList();
    static DiffUtil.ItemCallback<Article> itemCallback = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    };
    //public AsyncListDiffer<Article> differ = new AsyncListDiffer(this, itemCallback);
    //public AsyncPagingDataDiffer<Article> differ = new AsyncPagingDataDiffer<Article>(itemCallback,);

    public NewsAdapter(Context mContext,OnItemClickListener onItemClickListener) {
        super(itemCallback);
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
    }

    public Article getItemAt(int position)
    {
        return getItem(position);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article_preview, parent, false);
        ItemArticlePreviewBinding binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NewsViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
//        Article currentArticle = getCurrentList().get(holder.getAdapterPosition());
        Article currentArticle = getItem(position);
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
                        holder.articleProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.articleProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.articleImage);
        holder.articleTitle.setText(currentArticle.getTitle());
        holder.articleDesc.setText(currentArticle.getDescription());
        holder.articleSource.setText(currentArticle.getSource().getName());
        holder.articleTime.setText(" \u2022 " + Utils.DateToTimeFormat(currentArticle.getPublishedAt()));
        holder.articlePubAt.setText(Utils.DateFormat(currentArticle.getPublishedAt()));
        holder.articleAuthor.setText(currentArticle.getAuthor());
//        Glide.with(mContext).load(currentArticle.getUrlToImage()).into(holder.articleImage);
//        holder.articleTitle.setText(currentArticle.getTitle());
//        holder.articleSource.setText(currentArticle.getSource().getName());
//        holder.articleDesc.setText(currentArticle.getDescription());
//        holder.articlePubAt.setText(currentArticle.getPublishedAt());

    }




    public interface OnItemClickListener {
        void onItemClickListener(Article article);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView articleImage;
        TextView articleSource, articleTitle, articleDesc, articlePubAt,articleAuthor,articleTime;
        ProgressBar articleProgressBar;
        public NewsViewHolder(ItemArticlePreviewBinding binding) {
            super(binding.getRoot());
            articleTitle = binding.articleTitle;
            articleDesc = binding.articleDescription;
            articleImage = binding.articleImage;
            articleSource = binding.articleSource;
            articlePubAt = binding.articlePublishedAtTv;
            articleAuthor = binding.articleAuthor;
            articleTime = binding.articleTime;
            articleProgressBar = binding.articlePrograssLoadPhoto;
//            articleImage = itemView.findViewById(R.id.ivArticleImage);
//            articleSource = itemView.findViewById(R.id.tvSource);
//            articleTitle = itemView.findViewById(R.id.tvTitle);
//            articleDesc = itemView.findViewById(R.id.tvDescription);
//            articlePubAt = itemView.findViewById(R.id.tvPublishedAt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Article currentArticle = getCurrentList().get(getAdapterPosition());
            Article currentArticle = getItem(getBindingAdapterPosition());
            onItemClickListener.onItemClickListener(currentArticle);
        }
    }
}
