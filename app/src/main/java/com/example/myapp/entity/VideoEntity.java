package com.example.myapp.entity;

import java.io.Serializable;

/**
 * @author brynn
 * @since 2022/07/02 13:52
 */
public class VideoEntity implements Serializable {
 /**
                "vid": 6,
                "vtitle": "10斤用新鲜牛腿肉分享",
                "author": "美食作家王刚",
                "coverurl": "https://sf3-xgcdn-tos.pstatp.com/img/p1901/d9d5ae15079a8073f5cdb04b6a80777a~tplv-crop-center:1041:582.jpg",
                "headurl": "https://sf3-ttcdn-tos.pstatp.com/img/mosaic-legacy/da860012437af2fd24c2~360x360.image",
                "commentNum": 96,
                "likeNum": 700,
                "collectNum": 89,
                "playurl": "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4",
                "createTime": null,
                "updateTime": "2020-07-19 12:05:56",
                "categoryId": 3,
                "categoryName": "美食",
                "videoSocialEntity": null
 * */

    private int vid;
    private String vtitle;
    private String author;
    private String coverurl;
    private String headurl;
    private int commentNum;
    private int likeNum;
    private int collectNum;
    private String playurl;
    private String createTime;
    private String updateTime;
    private int categoryId;
    private String categoryName;
    private VideoSocialEntity videoSocialEntity;



    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public VideoSocialEntity getVideoSocialEntity() {
        return videoSocialEntity;
    }

    public void setVideoSocialEntity(VideoSocialEntity videoSocialEntity) {
        this.videoSocialEntity = videoSocialEntity;
    }

    public static class VideoSocialEntity {

        private boolean flagLike;
        private boolean flagCollect;

        public boolean isFlagLike() {
            return flagLike;
        }

        public void setFlagLike(boolean flagLike) {
            this.flagLike = flagLike;
        }

        public boolean isFlagCollect() {
            return flagCollect;
        }

        public void setFlagCollect(boolean flagCollect) {
            this.flagCollect = flagCollect;
        }
    }
}

