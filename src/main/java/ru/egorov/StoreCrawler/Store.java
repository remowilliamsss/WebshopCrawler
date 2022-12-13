package ru.egorov.StoreCrawler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
// TODO: 13.12.2022 должно быть в пакете моделей.
//  Старайся в названии подсветить, что класс - енам. StoreType, как вариант
public enum Store {
    SNEAKERHEAD("Sneakerhead"),
    FOOTBOX("Footbox");

    private final String name;

    @Override
    // TODO: 13.12.2022 зачем?
    public String toString() {
        return name;
    }
}
