package ru.egorov.StoreCrawler.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
// TODO: 13.12.2022 не хватает более детального разбиения на пакеты. Отдельный для продукт, отдельный для поиска и т.д.
public class ErrorResponse {
    private String message;
}
