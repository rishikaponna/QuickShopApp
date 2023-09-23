package com.example.quickshopapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product {
    //private static final long serialVersionUID = -4073256626483275668L;

    private String pId; // Changed to String
    private String pName;
    private BigDecimal pPrice;
    private String pDescription;
    private String pImageName;
    private boolean isFavorite;

    public Product() {
        super();
    }

    public Product(String pId, String pName, BigDecimal pPrice, String pDescription, String pImageName) {
        setId(pId);
        setName(pName);
        setPrice(pPrice);
        setDescription(pDescription);
        setImageName(pImageName);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getId() {
        return pId;
    }

    public void setId(String id) {
        this.pId = id;
    }

    public BigDecimal getPrice() {
        return pPrice;
    }

    public String getName() {
        return pName;
    }

    public void setPrice(BigDecimal price) {
        this.pPrice = price;
    }

    public void setName(String name) {
        this.pName = name;
    }

    public String getDescription() {
        return pDescription;
    }

    public void setDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getImageName() {
        return pImageName;
    }

    public void setImageName(String imageName) {
        this.pImageName = imageName;
    }
}
