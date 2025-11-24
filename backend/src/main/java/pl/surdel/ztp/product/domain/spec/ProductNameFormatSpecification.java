package pl.surdel.ztp.product.domain.spec;

import pl.surdel.ztp.product.domain.model.Product;

public class ProductNameFormatSpecification implements Specification<Product> {

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        if (candidate.name == null) {
            return false;
        }

        String name = candidate.name;

        if (name.length() < 3) return false;
        if (name.length() > 20) return false;

        return name.matches("^[a-zA-Z0-9 ]+$");
    }

    @Override
    public String message() {
        return "Product name must be between 3 and 20 characters long and contain only alphanumeric characters and spaces.";
    }
}
