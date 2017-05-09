package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/5/9.
 */

public class ReadingPageDetailVo implements Serializable {

    /**
     * res : 0
     * data : [{"item_id":"1276","title":"这一年，并没有那么糟","introduction":"2015年，也不知在哪一个刹那间，悄悄地过去了。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"1074","title":"爱的救赎","introduction":"人们总有一无所有却奋不顾身的年岁，也总有为了爱人而委曲求全的别样幸福，再经历些世故，又会有明知真相却不忍揭示的恻隐。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"972","title":"老菊的歌","introduction":"他说，我就是想看看，什么时候听到这首歌不再想吐了，那对我来说，这件事就算过去了。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"814","title":"识茶记","introduction":"后来，我换了一款茶喝，她换了一个人爱。 茶树过一季便有了新芽，山依然在海的两边，万世不变地矗立着。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"1104","title":"没什么比希望不平凡而更平凡的了","introduction":"小凤凰与老母鸡，天上的神鸟与地下的陋禽，这两人都有或多或少的孤独。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"1202","title":"风中有个肉做的人","introduction":"可别小看一个瘦下来的胖子。 你打不败他，因为在他身体里，有另一个已经被打败的自己。","author":"熊德启","web_url":"","number":0,"type":"1"},{"item_id":"1408","title":"藏在黑夜里的人","introduction":"藏在黑夜里的人，最怕闪电。  ","author":"熊德启","web_url":"","number":0,"type":"1"}]
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
         * item_id : 1276
         * title : 这一年，并没有那么糟
         * introduction : 2015年，也不知在哪一个刹那间，悄悄地过去了。
         * author : 熊德启
         * web_url :
         * number : 0
         * type : 1
         */

        private String item_id;
        private String title;
        private String introduction;
        private String author;
        private String web_url;
        private int number;
        private String type;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
