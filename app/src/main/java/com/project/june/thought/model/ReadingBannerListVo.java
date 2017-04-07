package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/4/7.
 */

public class ReadingBannerListVo implements Serializable {

    /**
     * res : 0
     * data : [{"id":"128","title":"潜能Skinner","cover":"http://image.wufazhuce.com/FqFj7Sk0JvJSVMzbc0JFgu0BrBaa","bottom_text":"世界上到底有没有成功的催眠大师？","bgcolor":"#d7560f","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/128"},{"id":"127","title":"这一年过的如何？","cover":"http://image.wufazhuce.com/Fr_r7e50MifiPsyWbFhsvcY3mfk_","bottom_text":"\u201c梦短梦长俱是梦，年来年去是何年。\u201d","bgcolor":"#24423A","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/127"},{"id":"126","title":"我们生来就是孤独","cover":"http://image.wufazhuce.com/FgUR18YWpOjS1p9t0ey_RxDSKzR3","bottom_text":"\u201c不管你拥有什么，我们生来就是孤独。\u201d","bgcolor":"#1c2454","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/126"},{"id":"125","title":"这一切并没有那么糟","cover":"http://image.wufazhuce.com/Fj_ePRtSc7yyPL3tf9kcQlhKdm0y","bottom_text":"\u201c以前觉得，勇敢就是粗暴地面对恐惧。后来发现，温柔也是勇敢。\u201d","bgcolor":"#124fa2","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/125"},{"id":"124","title":"七堇年专题","cover":"http://image.wufazhuce.com/FgDYdRk3BWNtbCtjkKIU5vo-ZWFP","bottom_text":"\u201c就让我们继续与生命的慷慨与繁华相爱；即使岁月以刻薄与荒芜相欺。\u201d","bgcolor":"#393135","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/124"},{"id":"123","title":"你再不来，我要下雪了","cover":"http://image.wufazhuce.com/Ftgg6J7j3qldjC_qDirU4x-OYFLp","bottom_text":"拥抱着就能取暖，依偎着便能生存。","bgcolor":"#f7bfbb","pv_url":"http://v3.wufazhuce.com:8000/api/reading/carousel/pv/123"}]
     */

    private int res;
    private List<DataBean> data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 128
         * title : 潜能Skinner
         * cover : http://image.wufazhuce.com/FqFj7Sk0JvJSVMzbc0JFgu0BrBaa
         * bottom_text : 世界上到底有没有成功的催眠大师？
         * bgcolor : #d7560f
         * pv_url : http://v3.wufazhuce.com:8000/api/reading/carousel/pv/128
         */

        private String id;
        private String title;
        private String cover;
        private String bottom_text;
        private String bgcolor;
        private String pv_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getBottom_text() {
            return bottom_text;
        }

        public void setBottom_text(String bottom_text) {
            this.bottom_text = bottom_text;
        }

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getPv_url() {
            return pv_url;
        }

        public void setPv_url(String pv_url) {
            this.pv_url = pv_url;
        }
    }
}
