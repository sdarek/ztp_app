package pl.surdel.ztp.product.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import pl.surdel.ztp.product.domain.model.ForbiddenWord;

@ApplicationScoped
public class ForbiddenWordRepository implements PanacheRepository<ForbiddenWord> {
}
