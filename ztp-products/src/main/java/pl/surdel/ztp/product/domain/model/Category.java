package pl.surdel.ztp.product.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    public String name;
}
