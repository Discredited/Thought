package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/3/28.
 */

public class SerializeDetailVo implements Serializable {


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

        private String id;
        private String serial_id;
        private String number;
        private String title;
        private String excerpt;
        private String content;
        private String charge_edt;
        private String read_num;
        private String maketime;
        private String last_update_date;
        private String hide_flag;
        private String audio;
        private String web_url;
        private String input_name;
        private String last_update_name;
        private String anchor;
        private String editor_email;
        private String top_media_type;
        private String top_media_file;
        private String top_media_image;
        private String start_video;
        private String copyright;
        private AuthorBean author;
        private String lastid;
        private String nextid;
        private ShareListBean share_list;
        private int praisenum;
        private int sharenum;
        private int commentnum;
        private List<AuthorListBean> author_list;
        private List<?> tag_list;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getExcerpt() {
            return excerpt;
        }

        public void setExcerpt(String excerpt) {
            this.excerpt = excerpt;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCharge_edt() {
            return charge_edt;
        }

        public void setCharge_edt(String charge_edt) {
            this.charge_edt = charge_edt;
        }

        public String getRead_num() {
            return read_num;
        }

        public void setRead_num(String read_num) {
            this.read_num = read_num;
        }

        public String getMaketime() {
            return maketime;
        }

        public void setMaketime(String maketime) {
            this.maketime = maketime;
        }

        public String getLast_update_date() {
            return last_update_date;
        }

        public void setLast_update_date(String last_update_date) {
            this.last_update_date = last_update_date;
        }

        public String getHide_flag() {
            return hide_flag;
        }

        public void setHide_flag(String hide_flag) {
            this.hide_flag = hide_flag;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getInput_name() {
            return input_name;
        }

        public void setInput_name(String input_name) {
            this.input_name = input_name;
        }

        public String getLast_update_name() {
            return last_update_name;
        }

        public void setLast_update_name(String last_update_name) {
            this.last_update_name = last_update_name;
        }

        public String getAnchor() {
            return anchor;
        }

        public void setAnchor(String anchor) {
            this.anchor = anchor;
        }

        public String getEditor_email() {
            return editor_email;
        }

        public void setEditor_email(String editor_email) {
            this.editor_email = editor_email;
        }

        public String getTop_media_type() {
            return top_media_type;
        }

        public void setTop_media_type(String top_media_type) {
            this.top_media_type = top_media_type;
        }

        public String getTop_media_file() {
            return top_media_file;
        }

        public void setTop_media_file(String top_media_file) {
            this.top_media_file = top_media_file;
        }

        public String getTop_media_image() {
            return top_media_image;
        }

        public void setTop_media_image(String top_media_image) {
            this.top_media_image = top_media_image;
        }

        public String getStart_video() {
            return start_video;
        }

        public void setStart_video(String start_video) {
            this.start_video = start_video;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public String getLastid() {
            return lastid;
        }

        public void setLastid(String lastid) {
            this.lastid = lastid;
        }

        public String getNextid() {
            return nextid;
        }

        public void setNextid(String nextid) {
            this.nextid = nextid;
        }

        public ShareListBean getShare_list() {
            return share_list;
        }

        public void setShare_list(ShareListBean share_list) {
            this.share_list = share_list;
        }

        public int getPraisenum() {
            return praisenum;
        }

        public void setPraisenum(int praisenum) {
            this.praisenum = praisenum;
        }

        public int getSharenum() {
            return sharenum;
        }

        public void setSharenum(int sharenum) {
            this.sharenum = sharenum;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }

        public List<AuthorListBean> getAuthor_list() {
            return author_list;
        }

        public void setAuthor_list(List<AuthorListBean> author_list) {
            this.author_list = author_list;
        }

        public List<?> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<?> tag_list) {
            this.tag_list = tag_list;
        }

        public static class AuthorBean {

            private String user_id;
            private String user_name;
            private String desc;
            private String wb_name;
            private String is_settled;
            private String settled_type;
            private String summary;
            private String fans_total;
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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getWb_name() {
                return wb_name;
            }

            public void setWb_name(String wb_name) {
                this.wb_name = wb_name;
            }

            public String getIs_settled() {
                return is_settled;
            }

            public void setIs_settled(String is_settled) {
                this.is_settled = is_settled;
            }

            public String getSettled_type() {
                return settled_type;
            }

            public void setSettled_type(String settled_type) {
                this.settled_type = settled_type;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getFans_total() {
                return fans_total;
            }

            public void setFans_total(String fans_total) {
                this.fans_total = fans_total;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }
        }

        public static class ShareListBean {

            private WxBean wx;
            private WeiboBean weibo;
            private QqBean qq;

            public WxBean getWx() {
                return wx;
            }

            public void setWx(WxBean wx) {
                this.wx = wx;
            }

            public WeiboBean getWeibo() {
                return weibo;
            }

            public void setWeibo(WeiboBean weibo) {
                this.weibo = weibo;
            }

            public QqBean getQq() {
                return qq;
            }

            public void setQq(QqBean qq) {
                this.qq = qq;
            }

            public static class WxBean {

                private String title;
                private String desc;
                private String link;
                private String imgUrl;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }

            public static class WeiboBean {

                private String title;
                private String desc;
                private String link;
                private String imgUrl;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }

            public static class QqBean {

                private String title;
                private String desc;
                private String link;
                private String imgUrl;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }
            }
        }

        public static class AuthorListBean {

            private String user_id;
            private String user_name;
            private String desc;
            private String wb_name;
            private String is_settled;
            private String settled_type;
            private String summary;
            private String fans_total;
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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getWb_name() {
                return wb_name;
            }

            public void setWb_name(String wb_name) {
                this.wb_name = wb_name;
            }

            public String getIs_settled() {
                return is_settled;
            }

            public void setIs_settled(String is_settled) {
                this.is_settled = is_settled;
            }

            public String getSettled_type() {
                return settled_type;
            }

            public void setSettled_type(String settled_type) {
                this.settled_type = settled_type;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getFans_total() {
                return fans_total;
            }

            public void setFans_total(String fans_total) {
                this.fans_total = fans_total;
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
