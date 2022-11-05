package ru.egorov.StoreCrawler.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sneakerhead_product")
public class SneakerheadProduct {
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
    private double price;

    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "gender")
    private String gender;

    @Column(name = "country")
    private String country;

    @Column(name = "sizes")
    private String sizes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SneakerheadProduct)) return false;
        SneakerheadProduct product = (SneakerheadProduct) o;
        return Double.compare(product.price, price) == 0 && name.equals(product.name) && sku.equals(product.sku) && Objects.equals(category, product.category) && Objects.equals(brand, product.brand) && Objects.equals(image, product.image) && Objects.equals(color, product.color) && Objects.equals(priceCurrency, product.priceCurrency) && Objects.equals(gender, product.gender) && Objects.equals(country, product.country) && Objects.equals(sizes, product.sizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sku, category, brand, image, color, price, priceCurrency, gender, country, sizes);
    }
}
