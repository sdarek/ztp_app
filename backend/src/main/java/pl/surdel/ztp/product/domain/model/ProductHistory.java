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

    @Column(name = "old_category", length = 50)
    public String oldCategory;

    @Column(name = "new_category", length = 50)
    public String newCategory;

    @Column(name = "old_price", precision = 10, scale = 2)
    public BigDecimal oldPrice;

    @Column(name = "new_price", precision = 10, scale = 2)
    public BigDecimal newPrice;

    @Column(name = "old_quantity")
    public Integer oldQuantity;

    @Column(name = "new_quantity")
    public Integer newQuantity;

    @Column(name = "changed_at", nullable = false)
    public OffsetDateTime changedAt;

    @PrePersist
    public void prePersist() {
        if (changedAt == null) {
            changedAt = OffsetDateTime.now();
        }
    }
}
