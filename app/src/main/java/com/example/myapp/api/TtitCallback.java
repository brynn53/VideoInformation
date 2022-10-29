package com.example.myapp.api;

/**
 * @author brynn
 * @since 2022/06/24 20:50
 */
public interface TtitCallback {
    void onSuccess(String res);
    void onFailure(Exception e);
}
