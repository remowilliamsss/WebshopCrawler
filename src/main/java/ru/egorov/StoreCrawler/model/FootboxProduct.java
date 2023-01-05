package ru.egorov.StoreCrawler.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "footbox_product")
@Getter
@Setter
public class FootboxProduct extends Product {
    private String composition;
    private String coloring;

    @Override
    @Transient
    public StoreType getStore() {
        return StoreType.footbox;
    }
}
