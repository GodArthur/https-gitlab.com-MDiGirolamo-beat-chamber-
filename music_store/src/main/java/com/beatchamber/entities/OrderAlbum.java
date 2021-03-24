/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beatchamber.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author kibra
 */
@Entity
@Table(name = "order_album")
@NamedQueries({
    @NamedQuery(name = "OrderAlbum.findAll", query = "SELECT o FROM OrderAlbum o"),
    @NamedQuery(name = "OrderAlbum.findByOrderId", query = "SELECT o FROM OrderAlbum o WHERE o.orderId = :orderId")})
public class OrderAlbum implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "order_id")
    private Integer orderId;
    @JoinColumn(name = "album_id", referencedColumnName = "album_number")
    @ManyToOne(optional = false)
    private Albums albumId;

    public OrderAlbum() {
    }

    public OrderAlbum(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Albums getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Albums albumId) {
        this.albumId = albumId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderAlbum)) {
            return false;
        }
        OrderAlbum other = (OrderAlbum) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.beatchamber.entities.OrderAlbum[ orderId=" + orderId + " ]";
    }
    
}
