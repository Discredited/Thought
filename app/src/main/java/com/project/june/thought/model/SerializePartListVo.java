package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/4/6.
 */

public class SerializePartListVo implements Serializable {


    /**
     * res : 0
     * data : {"id":"39","title":"暖气","finished":"1","list":[{"id":"245","serial_id":"39","number":"1"},{"id":"248","serial_id":"39","number":"2"},{"id":"249","serial_id":"39","number":"3"},{"id":"250","serial_id":"39","number":"4"},{"id":"251","serial_id":"39","number":"5"},{"id":"252","serial_id":"39","number":"6"},{"id":"254","serial_id":"39","number":"7"},{"id":"255","serial_id":"39","number":"8"},{"id":"256","serial_id":"39","number":"9"},{"id":"257","serial_id":"39","number":"10"},{"id":"258","serial_id":"39","number":"11"},{"id":"261","serial_id":"39","number":"12"},{"id":"268","serial_id":"39","number":"13"},{"id":"269","serial_id":"39","number":"14"},{"id":"270","serial_id":"39","number":"15"},{"id":"274","serial_id":"39","number":"16"},{"id":"275","serial_id":"39","number":"17"},{"id":"276","serial_id":"39","number":"18"},{"id":"279","serial_id":"39","number":"19"},{"id":"280","serial_id":"39","number":"20"},{"id":"281","serial_id":"39","number":"21"},{"id":"284","serial_id":"39","number":"22"},{"id":"285","serial_id":"39","number":"23"},{"id":"286","serial_id":"39","number":"24"}]}
     */

    private int res;
    private DataBean data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 39
         * title : 暖气
         * finished : 1
         * list : [{"id":"245","serial_id":"39","number":"1"},{"id":"248","serial_id":"39","number":"2"},{"id":"249","serial_id":"39","number":"3"},{"id":"250","serial_id":"39","number":"4"},{"id":"251","serial_id":"39","number":"5"},{"id":"252","serial_id":"39","number":"6"},{"id":"254","serial_id":"39","number":"7"},{"id":"255","serial_id":"39","number":"8"},{"id":"256","serial_id":"39","number":"9"},{"id":"257","serial_id":"39","number":"10"},{"id":"258","serial_id":"39","number":"11"},{"id":"261","serial_id":"39","number":"12"},{"id":"268","serial_id":"39","number":"13"},{"id":"269","serial_id":"39","number":"14"},{"id":"270","serial_id":"39","number":"15"},{"id":"274","serial_id":"39","number":"16"},{"id":"275","serial_id":"39","number":"17"},{"id":"276","serial_id":"39","number":"18"},{"id":"279","serial_id":"39","number":"19"},{"id":"280","serial_id":"39","number":"20"},{"id":"281","serial_id":"39","number":"21"},{"id":"284","serial_id":"39","number":"22"},{"id":"285","serial_id":"39","number":"23"},{"id":"286","serial_id":"39","number":"24"}]
         */

        private String id;
        private String title;
        private String finished;
        private List<ListBean> list;

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

        public String getFinished() {
            return finished;
        }

        public void setFinished(String finished) {
            this.finished = finished;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 245
             * serial_id : 39
             * number : 1
             */

            private String id;
            private String serial_id;
            private String number;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSerial_id() {
                return serial_id;
            }

            public void setSerial_id(String serial_id) {
                this.serial_id = serial_id;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }
    }
}
