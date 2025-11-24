package pl.surdel.ztp.product.domain.spec;

import pl.surdel.ztp.product.domain.model.ForbiddenWord;
import pl.surdel.ztp.product.domain.model.Product;
import pl.surdel.ztp.product.infrastructure.ForbiddenWordRepository;

import java.util.List;

public class ProductNameForbiddenWordsSpecification implements Specification<Product> {

    private final ForbiddenWordRepository forbiddenWordRepository;

    public ProductNameForbiddenWordsSpecification(ForbiddenWordRepository forbiddenWordRepository) {
        this.forbiddenWordRepository = forbiddenWordRepository;
    }

    @Override
    public boolean isSatisfiedBy(Product candidate) {
        if (candidate == null || candidate.name == null) {
            return false;
        }

        String nameLower = candidate.name.toLowerCase();
        List<ForbiddenWord> forbiddenWords = forbiddenWordRepository.listAll();
        for (ForbiddenWord forbiddenWord : forbiddenWords) {
            if (forbiddenWord.value == null) {
                continue;
            }
            String forbiddenLower = forbiddenWord.value.toLowerCase();
            if (!forbiddenLower.isEmpty() && nameLower.contains(forbiddenLower)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String message() {
        return "Product name contains forbidden words.";
    }
}
