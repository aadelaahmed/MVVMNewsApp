package com.androiddevs.mvvmnewsapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.androiddevs.mvvmnewsapp.model.Article;
import com.androiddevs.mvvmnewsapp.utils.Converters;

@Database(
        entities = {Article.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class ArticleDatabase extends RoomDatabase {
    public abstract ArticleDao getArticleDao();

    //private static ArticleDatabase articleDB;

//    public static synchronized ArticleDatabase getInstance(Context mContext) {
//        if (articleDB != null)
//            return articleDB;
//        articleDB =
//                Room.databaseBuilder(
//                        mContext.getApplicationContext(),
//                        ArticleDatabase.class,
//                        "Article Database"
//                )
//                        .fallbackToDestructiveMigration()
//                        .allowMainThreadQueries()
//                        .build();
//        return articleDB;
//    }
}
