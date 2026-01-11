package pl.surdel.ztp.product.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "product_history")
public class ProductHistory extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @Column(name = "old_name", length = 100)
    public String oldName;

    @Column(name = "new_name", length = 100)
    public String newName;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "old_category_id", nullable = true)
    public Category oldCategory;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "new_category_id", nullable = false)
    public Category newCategory;

    @Column(name = "old_price", precision = 10, scale = 2)
    public BigDecimal oldPrice;

    @Column(name = "new_price", precision = 10, scale = 2)
    public BigDecimal newPrice;

    @Column(name = "old_quantity")
    public Integer oldQuantity;

    @Column(name = "new_quantity")
    public Integer newQuantity;

    @Column(name = "product_created_at", nullable = false)
    public OffsetDateTime productCreatedAt;

    @Column(name = "product_updated_at", nullable = false)
    public OffsetDateTime productUpdatedAt;

    public static ProductHistory createFrom(Product oldProduct, Product newProduct, OffsetDateTime productCreatedAt) {
        ProductHistory history = new ProductHistory();
        history.product = newProduct;
        history.oldName = oldProduct != null ? oldProduct.name : null;
        history.oldCategory = oldProduct != null ? oldProduct.category : null;
        history.oldPrice = oldProduct != null ? oldProduct.price : null;
        history.oldQuantity = oldProduct != null ? oldProduct.quantity : null;

        history.newName = newProduct.name;
        history.newCategory = newProduct.category;
        history.newPrice = newProduct.price;
        history.newQuantity = newProduct.quantity;

        if (productCreatedAt != null) {
            history.productCreatedAt = productCreatedAt;
            history.productUpdatedAt = OffsetDateTime.now();
        } else {
            OffsetDateTime now = OffsetDateTime.now();
            history.productCreatedAt = now;
            history.productUpdatedAt = now;
        }
        return history;
    }

    public static ProductHistory createFrom(Product newProduct) {
        return createFrom(null, newProduct, null);
    }

    @PrePersist
    public void prePersist() {
        if (productUpdatedAt == null) {
            productUpdatedAt = OffsetDateTime.now();
        }
        if (productCreatedAt == null) {
            productCreatedAt = productUpdatedAt;
        }
    }
}
