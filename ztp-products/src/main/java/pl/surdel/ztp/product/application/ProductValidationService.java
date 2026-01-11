package pl.surdel.ztp.product.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pl.surdel.ztp.product.domain.exception.DomainValidationException;
import pl.surdel.ztp.product.domain.model.Product;
import pl.surdel.ztp.product.domain.spec.*;
import pl.surdel.ztp.product.infrastructure.ForbiddenWordRepository;
import pl.surdel.ztp.product.infrastructure.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductValidationService {
    @Inject
    ProductRepository productRepository;
    @Inject
    ForbiddenWordRepository forbiddenWordRepository;

    public void validate(Product product) {
        CompositeSpecification<Product> spec = new CompositeSpecification<>(
                new ProductNameFormatSpecification(),
                new ProductQuantityNonNegativeSpecification(),
                new ProductPriceRangeSpecification(),
                new ProductNameForbiddenWordsSpecification(forbiddenWordRepository),
                new ProductNameUniqueSpecification(productRepository)
        );

        List<String> errors = spec.validateAll(product);
        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }
}
