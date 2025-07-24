package kr.hhplus.be.server.product.reader;

import kr.hhplus.be.server.product.domain.Product;

public interface ProductReader {
    String getNameById(Long productId);
    Product getById(Long productId);
}