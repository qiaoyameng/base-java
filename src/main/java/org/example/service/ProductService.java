package org.example.service;

import org.example.dto.ProductDTO;
import org.example.dto.ProductQueryDTO;
import org.example.entity.Product;
import org.example.common.PageResult;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    PageResult<ProductDTO> listProducts(ProductQueryDTO queryDTO);

    void updateProductStatus(Long id, String status);

    boolean deductStock(Long productId, Integer quantity);

    void increaseSales(Long productId, Integer quantity);
}
