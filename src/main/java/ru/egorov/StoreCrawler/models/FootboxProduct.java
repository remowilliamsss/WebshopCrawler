package ru.egorov.StoreCrawler.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "footbox_product")
public class FootboxProduct extends Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // TODO: 20.11.2022 Почти любая ORM тебе автоматически соотнесет названия,
    //  если поле и колонка названы одинаково
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

    // TODO: 20.11.2022 Не уверен, давно не трогал хибернейт, но такое он вроде тоже сам умеет мапить
    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "gender")
    private String gender;

    @Column(name = "country")
    private String country;

    @Column(name = "sizes")
    private String sizes;

    @Column(name = "composition")
    private String composition;

    @Column(name = "coloring")
    private String coloring;

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

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getColoring() {
        return coloring;
    }

    public void setColoring(String coloring) {
        this.coloring = coloring;
    }

    @Override
    // TODO: 20.11.2022 if или цикл без без {} - зло. Даже если в одну строку.
    //  И даже если идея сама это сгенерила
    public boolean equals(Object o) {
        if (this == o) return true;
        // TODO: 20.11.2022 Лучше сравнивать по .getClass(), если выбор instanceof не был осознанным
        if (!(o instanceof FootboxProduct)) return false;
        FootboxProduct that = (FootboxProduct) o;
        // TODO: 20.11.2022 С equals энтити вообще стоит быть поаккуратней, они замедляют работу ORM.
        //  Но даже если он тебе нужен - почему недостаточно сравнения по id или другим уникальные полям?
        //  Зачем сравнивать вообще все?
        //  И никогда не делай это в одну строку:)
        return name.equals(that.name) && sku.equals(that.sku) && Objects.equals(category, that.category) && Objects.equals(brand, that.brand) && Objects.equals(image, that.image) && Objects.equals(color, that.color) && Objects.equals(price, that.price) && Objects.equals(priceCurrency, that.priceCurrency) && Objects.equals(gender, that.gender) && Objects.equals(country, that.country) && Objects.equals(sizes, that.sizes) && Objects.equals(composition, that.composition) && Objects.equals(coloring, that.coloring);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sku, category, brand, image, color, price, priceCurrency, gender, country, sizes, composition, coloring);
    }
}
