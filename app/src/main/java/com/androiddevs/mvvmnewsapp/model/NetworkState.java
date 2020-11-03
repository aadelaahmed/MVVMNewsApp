package com.androiddevs.mvvmnewsapp.model;

public class NetworkState {
    public enum Status{
        LOADED,
        LOADING,
        ERROR,
        ENDOFLIST
    }

    public static final NetworkState LOADED = new NetworkState(Status.LOADED,"loaded successfully");
    public static final NetworkState LOADING = new NetworkState(Status.LOADING,"Loading....");
    public static final NetworkState ERROR = new NetworkState(Status.ERROR,"somthing went wrong");
    public static final NetworkState ENDOFLIST = new NetworkState(Status.LOADED,"end of the list");


    private String message;
    private Status status;
    public NetworkState(Status status,String message)
    {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
