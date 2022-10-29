package com.example.myapp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author brynn
 * @since 2022/10/14 15:49
 */
public class NewsListResponse implements Serializable {
    /*
    *"msg": "success",
    "code": 0,
    "page": {
        "totalCount": 8,
        "pageSize": 10,
        "totalPage": 1,
        "currPage": 1,
        "list": [
            {
                "newsId": 1,
                "newsTitle": "《忍者蛙》发售日公布 已上架Steam、支持简中",
                "authorName": "3DMGAME",
                "headerUrl": "https://p9.pstatp.com/thumb/6eed00026c4eac713a44",
                "commentCount": 3,
                "releaseDate": "2020-07-31 22:23:00",
                "type": 1,
                "thumbEntities": [
                    {
                        "thumbId": 1,
                        "thumbUrl": "http://p1-tt.byteimg.com/large/pgc-image/S6KR5958Y5X2Qt?from=pc",
                        "newsId": 1
                    }
                ],
                "imgList": null
            } ...... ] } }
    * */
    private String msg;
    private int code;
    private PageBean page;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PageBean getPageBean() {
        return page;
    }

    public void setPageBean(PageBean pageBean) {
        this.page = pageBean;
    }

    public static class PageBean{
        private int totalCount;
        private int pageSize;
        private int totalPage;
        private int currPage;
        private List<NewsEntity> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<NewsEntity> getList() {
            return list;
        }

        public void setList(List<NewsEntity> list) {
            this.list = list;
        }
    }
}
