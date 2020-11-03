package com.androiddevs.mvvmnewsapp.utils;


import androidx.room.TypeConverter;

import com.androiddevs.mvvmnewsapp.model.Source;
import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public String fromSourceToJson(Source source)
    {
        return new Gson().toJson(source);
    }
    @TypeConverter
    public Source fromJsonToSource(String json)
    {
        return new Gson().fromJson(json,Source.class);
    }
}
