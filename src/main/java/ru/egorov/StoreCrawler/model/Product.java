package ru.egorov.StoreCrawler.model;

import lombok.Getter;
import lombok.Setter;
import ru.egorov.StoreCrawler.Store;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
@Getter
@Setter
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Column(unique = true)
    private String sku;
    private String category;
    private String brand;
    private String image;
    private String color;
    private Double price;
    private String priceCurrency;
    private String country;
    private String sizes;
    private String gender;
    @NotEmpty
    @Column(unique = true)
    private String url;

    @Transient
    public abstract Store getStore();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
