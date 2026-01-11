package pl.surdel.ztp.product.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    public String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    public Category category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    @Column(name = "quantity", nullable = false)
    public Integer quantity;

    public Product copy() {
        Product product = new Product();
        product.id = this.id;
        product.name = this.name;
        product.category = this.category;
        product.price = this.price;
        product.quantity = this.quantity;
        return product;
    }

    public void update(Product product) {
        this.name = product.name;
        this.category = product.category;
        this.price = product.price;
        this.quantity = product.quantity;
    }
}
