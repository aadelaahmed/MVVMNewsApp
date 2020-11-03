package com.androiddevs.mvvmnewsapp.utils;

public class Resource<T>{
    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        ENDOFLIST
    }


    String msg;
    Status status;
    T data;

    public static Resource LOADED = new Resource(Status.SUCCESS,"Success")
            ,LOADING = new Resource(Status.RUNNING,"Running")
            ,ERROR = new Resource(Status.FAILED,"Something went wrong")
            ,ENDOFLIST = new Resource(Status.ENDOFLIST,"you have reached the end of list");

    public Resource(Status status, String msg)
    {
        this.msg = msg;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

//public abstract class Resource<T> {
//
//
//
//    private Resource(T data, String message) {
//
//    }
//
//    public static final class Success<T> extends Resource<T> {
//
//        public Success(T data) {
//            super(data, null);
//        }
//    }
//
//    public static final class Error<T> extends Resource<T> {
//
//        public Error(T data, String message) {
//            super(data, message);
//        }
//    }
//
//    public static final class Loading<T> extends Resource<T> {
//
//        public Loading() {
//            super(null, null);
//        }
//    }
//
//
//}
