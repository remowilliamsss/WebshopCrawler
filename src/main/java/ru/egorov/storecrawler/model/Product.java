package ru.egorov.storecrawler.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@MappedSuperclass
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

    private String size;

    private String gender;

    @NotEmpty
    @Column(unique = true)
    private String url;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
