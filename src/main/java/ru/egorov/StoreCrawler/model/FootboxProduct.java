package ru.egorov.StoreCrawler.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "footbox_product")
public class FootboxProduct extends Product {

    private String composition;

    private String coloring;

    {
        setStoreType(StoreType.footbox);
    }
}
