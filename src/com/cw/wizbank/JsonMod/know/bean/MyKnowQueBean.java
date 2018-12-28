package com.cw.wizbank.JsonMod.know.bean;

import java.util.Vector;

/**
 * @author DeanChen
 * 
 */
public class MyKnowQueBean {

    private boolean is_my_que;

    private int total;

    private String sort;

    private String dir;

    private Vector que_lst;

    public boolean isIs_my_que() {
        return is_my_que;
    }

    public void setIs_my_que(boolean is_my_que) {
        this.is_my_que = is_my_que;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Vector getQue_lst() {
        return que_lst;
    }

    public void setQue_lst(Vector que_lst) {
        this.que_lst = que_lst;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

}
