package pl.surdel.ztp.product.domain.spec;

import pl.surdel.ztp.product.domain.model.Product;

import java.math.BigDecimal;

public class ProductPriceRangeSpecification implements Specification<Product> {
    @Override
    public boolean isSatisfiedBy(Product candidate) {
        if (candidate == null || candidate.price == null || candidate.category.name == null) {
            return false;
        }

        BigDecimal price = candidate.price;
        String category = candidate.category.name.trim().toUpperCase();

        return switch (category) {
            case "ELECTRONICS" -> inRange(price, bd("50"), bd("100000"));
            case "BOOKS" -> inRange(price, bd("5"), bd("1000"));
            case "CLOTHES" -> inRange(price, bd("10"), bd("5000"));
            default -> false;
        };
    }

    @Override
    public String message() {
        return "Product price is out of the allowed range for its category or category doesn't exist.";
    }

    private BigDecimal bd(String price) {
        return new BigDecimal(price);
    }

    private boolean inRange(BigDecimal price, BigDecimal min, BigDecimal max) {
        return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
    }
}
