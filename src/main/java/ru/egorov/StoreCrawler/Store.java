package ru.egorov.StoreCrawler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Store {
    SNEAKERHEAD("Sneakerhead"),
    FOOTBOX("Footbox");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
