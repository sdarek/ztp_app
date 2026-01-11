package pl.surdel.ztp.product.api.dto;

import pl.surdel.ztp.product.domain.model.Category;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ProductHistoryResponse {
    public String oldName;
    public String oldCategory;
    public BigDecimal oldPrice;
    public Integer oldQuantity;

    public String newName;
    public String newCategory;
    public BigDecimal newPrice;
    public Integer newQuantity;
    public OffsetDateTime productCreatedAt;
    public OffsetDateTime productUpdatedAt;
}
