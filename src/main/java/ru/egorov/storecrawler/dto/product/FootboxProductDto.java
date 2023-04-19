package ru.egorov.storecrawler.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FootboxProductDto extends ProductDto {

    @Schema(description = "состав")
    private String composition;

    @Schema(description = "расцветка")
    private String coloring;
}
