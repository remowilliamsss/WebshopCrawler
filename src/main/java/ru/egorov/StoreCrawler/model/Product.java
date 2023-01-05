package ru.egorov.StoreCrawler.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
@Getter
@Setter
public abstract class Product {
    // TODO: 13.12.2022 я советую для классов-сущностей (не только хибовских Entity) 
    //  поля разделять пустыми строками. Иначе сливается, особенно, когда еще и аннотации навешаны
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
    // TODO: 13.12.2022 технически верное, но спорно решение. Почему бы не хранить поле type? 
    //  на одну колонку в таблицах больше, но с логикой работать проще 
    public abstract StoreType getStore();

    @Override
    public boolean equals(Object o) {
        // TODO: 13.12.2022 использовать if без {} недопустимо. Пусть даже это и автогенерация
        if (this == o) return true;
        // TODO: 13.12.2022 потеряна проверка на null
        if (getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
