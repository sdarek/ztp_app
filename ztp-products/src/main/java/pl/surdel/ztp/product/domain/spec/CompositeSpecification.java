package pl.surdel.ztp.product.domain.spec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeSpecification<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    @SafeVarargs
    public CompositeSpecification(Specification<T>... specifications) {
        this.specifications.addAll(Arrays.asList(specifications));
    }

    public List<String> validateAll(T candidate) {
        List<String> errors = new ArrayList<>();
        for (Specification<T> spec : specifications) {
            if (!spec.isSatisfiedBy(candidate)) {
                errors.add(spec.message());
            }
        }
        return errors;
    }
}
