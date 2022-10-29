package com.example.myapp.api;

/**
 * @author brynn
 * @since 2022/06/24 21:03
 */
public class ApiConfig {
    public static final int PAGE_SIZE = 5;
    public static final String BASE_URl = "http://192.168.3.206:8080/renren-fast";
//    public static final String BASE_URl = "http://172.20.10.2:8080/renren-fast";
    public static final String LOGIN = "/app/login"; //登录
    public static final String REGISTER = "/app/register";
    public static final String VIDEO_LIST = "/app/videolist/list"; //所有类型视频列表
    public static final String VIDEO_CATEGORY_LIST = "/app/videocategory/list";//视频类型列表
    public static final String VIDEO_LIST_BY_CATEGORY = "/app/videolist/getListByCategoryId";//各类型视频列表
    public static final String NEWS_LIST = "/app/news/api/list"; //资讯列表
}
