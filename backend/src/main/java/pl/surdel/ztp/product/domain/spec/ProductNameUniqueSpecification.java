package pl.surdel.ztp.product.domain.spec;

import pl.surdel.ztp.product.domain.model.Product;
import pl.surdel.ztp.product.infrastructure.ProductRepository;

import java.util.Optional;

public class ProductNameUniqueSpecification implements Specification<Product> {

    private final ProductRepository productRepository;

    public ProductNameUniqueSpecification(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        if (candidate == null || candidate.name == null) {
            return false;
        }

        Optional<Product> existingProductOpt = productRepository
                .find("name", candidate.name)
                .firstResultOptional();

        if (existingProductOpt.isEmpty()) {
            return true;
        }

        Product existingProduct = existingProductOpt.get();

        return existingProduct.id.equals(candidate.id);
    }

    @Override
    public String message() {
        return "Product name must be unique.";
    }
}
