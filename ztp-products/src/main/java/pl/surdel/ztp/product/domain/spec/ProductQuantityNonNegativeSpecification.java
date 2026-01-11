package pl.surdel.ztp.product.domain.spec;

import pl.surdel.ztp.product.domain.model.Product;

public class ProductQuantityNonNegativeSpecification implements Specification<Product> {
    @Override
    public boolean isSatisfiedBy(Product candidate) {
        if (candidate == null || candidate.quantity == null) {
            return false;
        }
        return candidate.quantity >= 0;
    }

    @Override
    public String message() {
        return "Product quantity must be greater than or equal to 0.";
    }
}
