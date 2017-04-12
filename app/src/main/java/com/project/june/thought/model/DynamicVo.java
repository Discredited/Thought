package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/3/27.
 */

public class DynamicVo implements Serializable {

    /**
     * res : 0
     * data : {"count":41,"data":[{"id":"51172","quote":"一夫一妻制其实也够了，你想啊，一个夫人一个妻子，这就两个了，够你折腾了！","content":"此夫为丈夫","praisenum":2,"device_token":"","del_flag":"0","reviewed":"0","user_info_id":"0","input_date":"2017-04-05 13:15:10","created_at":"2017-04-05 13:15:10","updated_at":"0000-00-00 00:00:00","user":{"user_id":"7912761","user_name":"Ly.","web_url":"http://wx.qlogo.cn/mmopen/hLxK5cQqoPYVNUHbEY57ElLkdT1P0r2s4r7zu3ffLEyM5JotKz8vBBCsric5jsG4qcrfWY8AqeJ9kpT9WtQxqftpb2b97FTm8/0"},"touser":{"user_id":"5861167","user_name":"醇藝白","web_url":"http://q.qlogo.cn/qqapp/1104596227/921DED7DEDD141FFDD868B033BD109F8/100"},"type":1}]}
     */

    private int res;
    private DataBeanX data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * count : 41
         * data : [{"id":"51172","quote":"一夫一妻制其实也够了，你想啊，一个夫人一个妻子，这就两个了，够你折腾了！","content":"此夫为丈夫","praisenum":2,"device_token":"","del_flag":"0","reviewed":"0","user_info_id":"0","input_date":"2017-04-05 13:15:10","created_at":"2017-04-05 13:15:10","updated_at":"0000-00-00 00:00:00","user":{"user_id":"7912761","user_name":"Ly.","web_url":"http://wx.qlogo.cn/mmopen/hLxK5cQqoPYVNUHbEY57ElLkdT1P0r2s4r7zu3ffLEyM5JotKz8vBBCsric5jsG4qcrfWY8AqeJ9kpT9WtQxqftpb2b97FTm8/0"},"touser":{"user_id":"5861167","user_name":"醇藝白","web_url":"http://q.qlogo.cn/qqapp/1104596227/921DED7DEDD141FFDD868B033BD109F8/100"},"type":1}]
         */

        private int count;
        private List<DataBean> data;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 51172
             * quote : 一夫一妻制其实也够了，你想啊，一个夫人一个妻子，这就两个了，够你折腾了！
             * content : 此夫为丈夫
             * praisenum : 2
             * device_token :
             * del_flag : 0
             * reviewed : 0
             * user_info_id : 0
             * input_date : 2017-04-05 13:15:10
             * created_at : 2017-04-05 13:15:10
             * updated_at : 0000-00-00 00:00:00
             * user : {"user_id":"7912761","user_name":"Ly.","web_url":"http://wx.qlogo.cn/mmopen/hLxK5cQqoPYVNUHbEY57ElLkdT1P0r2s4r7zu3ffLEyM5JotKz8vBBCsric5jsG4qcrfWY8AqeJ9kpT9WtQxqftpb2b97FTm8/0"}
             * touser : {"user_id":"5861167","user_name":"醇藝白","web_url":"http://q.qlogo.cn/qqapp/1104596227/921DED7DEDD141FFDD868B033BD109F8/100"}
             * type : 1
             */

            private String id;
            private String quote;
            private String content;
            private int praisenum;
            private String device_token;
            private String del_flag;
            private String reviewed;
            private String user_info_id;
            private String input_date;
            private String created_at;
            private String updated_at;
            private UserBean user;
            private TouserBean touser;
            private int type;
            private Boolean isLaud = false;

            public Boolean getLaud() {
                return isLaud;
            }

            public void setLaud(Boolean laud) {
                isLaud = laud;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getQuote() {
                return quote;
            }

            public void setQuote(String quote) {
                this.quote = quote;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPraisenum() {
                return praisenum;
            }

            public void setPraisenum(int praisenum) {
                this.praisenum = praisenum;
            }

            public String getDevice_token() {
                return device_token;
            }

            public void setDevice_token(String device_token) {
                this.device_token = device_token;
            }

            public String getDel_flag() {
                return del_flag;
            }

            public void setDel_flag(String del_flag) {
                this.del_flag = del_flag;
            }

            public String getReviewed() {
                return reviewed;
            }

            public void setReviewed(String reviewed) {
                this.reviewed = reviewed;
            }

            public String getUser_info_id() {
                return user_info_id;
            }

            public void setUser_info_id(String user_info_id) {
                this.user_info_id = user_info_id;
            }

            public String getInput_date() {
                return input_date;
            }

            public void setInput_date(String input_date) {
                this.input_date = input_date;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public TouserBean getTouser() {
                return touser;
            }

            public void setTouser(TouserBean touser) {
                this.touser = touser;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public static class UserBean {
                /**
                 * user_id : 7912761
                 * user_name : Ly.
                 * web_url : http://wx.qlogo.cn/mmopen/hLxK5cQqoPYVNUHbEY57ElLkdT1P0r2s4r7zu3ffLEyM5JotKz8vBBCsric5jsG4qcrfWY8AqeJ9kpT9WtQxqftpb2b97FTm8/0
                 */

                private String user_id;
                private String user_name;
                private String web_url;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getWeb_url() {
                    return web_url;
                }

                public void setWeb_url(String web_url) {
                    this.web_url = web_url;
                }
            }

            public static class TouserBean {
                /**
                 * user_id : 5861167
                 * user_name : 醇藝白
                 * web_url : http://q.qlogo.cn/qqapp/1104596227/921DED7DEDD141FFDD868B033BD109F8/100
                 */

                private String user_id;
                private String user_name;
                private String web_url;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getWeb_url() {
                    return web_url;
                }

                public void setWeb_url(String web_url) {
                    this.web_url = web_url;
                }
            }
        }
    }
}
