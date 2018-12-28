package com.cw.wizbank.JsonMod.know.bean;

import java.util.Vector;

/**
 * @author DeanChen
 * 
 */
public class CatalogNavBean {

    private int id;

    private String title;

    private int order;

    private Vector parent_nav;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Vector getParent_nav() {
        return parent_nav;
    }

    public void setParent_nav(Vector parent_nav) {
        this.parent_nav = parent_nav;
    }

}
