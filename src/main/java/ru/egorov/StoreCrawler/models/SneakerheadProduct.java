package ru.egorov.StoreCrawler.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sneakerhead_product")
public class SneakerheadProduct extends Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "sku")
    private String sku;

    @Column(name = "category")
    private String category;

    @Column(name = "brand")
    private String brand;

    @Column(name = "image")
    private String image;

    @Column(name = "color")
    private String color;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "country")
    private String country;

    @Column(name = "sizes")
    private String sizes;

    @Column(name = "gender")
    private String gender;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSizes() {
        return sizes;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SneakerheadProduct)) return false;
        SneakerheadProduct that = (SneakerheadProduct) o;
        return name.equals(that.name) && sku.equals(that.sku) && Objects.equals(category, that.category) && Objects.equals(brand, that.brand) && Objects.equals(image, that.image) && Objects.equals(color, that.color) && Objects.equals(price, that.price) && Objects.equals(priceCurrency, that.priceCurrency) && Objects.equals(country, that.country) && Objects.equals(sizes, that.sizes) && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sku, category, brand, image, color, price, priceCurrency, country, sizes, gender);
    }
}
