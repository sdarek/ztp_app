package pl.surdel.ztp.product.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pl.surdel.ztp.product.api.dto.ProductRequest;
import pl.surdel.ztp.product.domain.exception.DomainValidationException;
import pl.surdel.ztp.product.domain.model.Category;
import pl.surdel.ztp.product.domain.model.Product;
import pl.surdel.ztp.product.domain.model.ProductHistory;
import pl.surdel.ztp.product.infrastructure.CategoryRepository;
import pl.surdel.ztp.product.infrastructure.ProductHistoryRepository;
import pl.surdel.ztp.product.infrastructure.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    ProductHistoryRepository productHistoryRepository;
    @Inject
    CategoryRepository categoryRepository;
    @Inject
    ProductValidationService validationService;

    public List<Product> getAllProducts() {
        return productRepository.listAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    public List<ProductHistory> getProductHistory(Long id) {
        return productHistoryRepository
                .find("product.id = ?1 ORDER BY productUpdatedAt", id)
                .list();
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.name = request.name;
        product.category = findCategoryByNameOrThrow(request.category);
        product.price = new BigDecimal(request.price);
        product.quantity = request.quantity;

        validationService.validate(product);
        productRepository.persist(product);

        ProductHistory productHistory = ProductHistory.createFrom(product);
        productHistoryRepository.persist(productHistory);

        return product;
    }
    @Transactional
    public Optional<Product> updateProduct(Long id, ProductRequest request) {
        Optional<Product> existingProductOpt = productRepository.findByIdOptional(id);
        if (existingProductOpt.isEmpty()) {
            return Optional.empty();
        }

        Product existingProduct = existingProductOpt.get();

        Product candidate = existingProduct.copy();
        candidate.name = request.name;
        candidate.category = findCategoryByNameOrThrow(request.category);
        candidate.price = new BigDecimal(request.price);
        candidate.quantity = request.quantity;

        // 2. Walidacja NA KANDYDACIE – tu NIE ma flush
        validationService.validate(candidate);

        // 3. Kopia starego stanu do historii
        Product oldProduct = existingProduct.copy();

        // 4. Teraz dopiero zmieniamy encję z bazy
        existingProduct.update(candidate);

        productRepository.persist(existingProduct);

        // 5. Historia
        recordHistory(oldProduct, existingProduct);

        return Optional.of(existingProduct);
    }


    @Transactional
    public boolean deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }

    private void recordHistory(Product oldProduct, Product newProduct) {
        ProductHistory firstHistory = productHistoryRepository
                .find("product", oldProduct)
                .firstResult();

        ProductHistory productHistory = ProductHistory.createFrom(
                oldProduct,
                newProduct,
                firstHistory != null ? firstHistory.productCreatedAt : null
        );

        productHistoryRepository.persist(productHistory);
    }

    private Category findCategoryByNameOrThrow(String name) {
        return categoryRepository
                .find("name", name)
                .firstResultOptional()
                .orElseThrow(() ->
                        new DomainValidationException(
                                List.of("Category '%s' does not exist".formatted(name))
                        )
                );
    }
}
