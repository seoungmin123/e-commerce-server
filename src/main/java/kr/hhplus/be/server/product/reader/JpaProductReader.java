package kr.hhplus.be.server.product.reader;

import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaProductReader implements ProductReader {

    private final ProductRepository productRepository;

    @Override
    public String getNameById(Long productId) {
        return productRepository.findById(productId)
                .map(Product::getName)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}