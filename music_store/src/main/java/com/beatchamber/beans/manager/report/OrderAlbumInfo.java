/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beatchamber.beans.manager.report;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Yan Tang
 */
@Named
@RequestScoped
public class OrderAlbumInfo {

    private int albumid;

    private String albumTitle;

    private double listPrice;

    private double salePrice;

    private Date orderDate;

    public OrderAlbumInfo() {

    }

    public OrderAlbumInfo(int albumid, String albumTitle, double listPrice, double salePrice, Date orderDate) {
        this.albumid = albumid;
        this.albumTitle = albumTitle;
        this.listPrice = listPrice;
        this.salePrice = salePrice;
        this.orderDate = orderDate;
    }

    public int getAlbumId() {
        return albumid;
    }

    public double getListPrice() {
        return listPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

}
