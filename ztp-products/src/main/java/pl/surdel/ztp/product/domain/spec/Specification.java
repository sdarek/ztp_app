package pl.surdel.ztp.product.domain.spec;

public interface Specification<T> {

    boolean isSatisfiedBy(T candidate);

    String message();
}
