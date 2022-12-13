package ru.egorov.StoreCrawler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO: 13.12.2022 сеттер лишний.
//  Я бы советовал запросы именовать как request, а респонсы как dto. но это вкусовщина
public abstract class ProductDto {// TODO: 13.12.2022 если используются только наследники, этот класс должен быть абстрактным
    private String name;
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
    private String url;
}
