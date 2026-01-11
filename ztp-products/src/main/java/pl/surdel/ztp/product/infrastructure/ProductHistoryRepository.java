package pl.surdel.ztp.product.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pl.surdel.ztp.product.domain.model.ProductHistory;

@ApplicationScoped
public class ProductHistoryRepository implements PanacheRepository<ProductHistory> {
}
