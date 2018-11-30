package com.zmx.mobilecashier.bean;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-28 21:48
 * 类功能：商品分类
 */

public class Group{

        private int id;
        private String gname;
        private int state;
        private int mid;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }
        public String getGname() {
            return gname;
        }

        public void setState(int state) {
            this.state = state;
        }
        public int getState() {
            return state;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }
        public int getMid() {
            return mid;
        }


}
