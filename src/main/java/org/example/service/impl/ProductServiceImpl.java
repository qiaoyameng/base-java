package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.dto.ProductQueryDTO;
import org.example.dto.ProductSpecificationDTO;
import org.example.entity.Product;
import org.example.entity.ProductSpecification;
import org.example.enums.ProductStatus;
import org.example.common.PageResult;
import org.example.repository.ProductRepository;
import org.example.repository.ProductSpecificationRepository;
import org.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository specificationRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setId(null);
        product.setDeleted(false);
        product.setSalesCount(0);
        product.setRating(5.0);

        if (productDTO.getStatus() == null) {
            product.setStatus(ProductStatus.ON_SALE);
        }

        Product savedProduct = productRepository.save(product);

        if (productDTO.getSpecifications() != null && !productDTO.getSpecifications().isEmpty()) {
            List<ProductSpecification> specifications = new ArrayList<>();
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                BeanUtils.copyProperties(specDTO, spec);
                spec.setProduct(savedProduct);
                specifications.add(spec);
            }
            specificationRepository.saveAll(specifications);
            savedProduct.setSpecifications(specifications);
        }

        return convertToDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        BeanUtils.copyProperties(productDTO, product, "id", "createTime", "deleted", "salesCount", "rating");
        Product updatedProduct = productRepository.save(product);

        if (productDTO.getSpecifications() != null) {
            specificationRepository.deleteByProductId(id);
            List<ProductSpecification> specifications = new ArrayList<>();
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                BeanUtils.copyProperties(specDTO, spec);
                spec.setId(null);
                spec.setProduct(updatedProduct);
                specifications.add(spec);
            }
            specificationRepository.saveAll(specifications);
            updatedProduct.setSpecifications(specifications);
        }

        return convertToDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return convertToDTO(product);
    }

    @Override
    public PageResult<ProductDTO> listProducts(ProductQueryDTO queryDTO) {
        Pageable pageable = PageRequest.of(queryDTO.getPageNum() - 1, queryDTO.getPageSize());
        Page<Product> productPage = productRepository.findByConditions(
                queryDTO.getName(),
                queryDTO.getCategory(),
                queryDTO.getStatus(),
                queryDTO.getMinPrice(),
                queryDTO.getMaxPrice(),
                pageable
        );

        List<ProductDTO> dtoList = productPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        Page<ProductDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, productPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public void updateProductStatus(Long id, String status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        product.setStatus(ProductStatus.valueOf(status));
        productRepository.save(product);
    }

    @Override
    @Transactional
    public boolean deductStock(Long productId, Integer quantity) {
        int result = productRepository.deductStock(productId, quantity);
        return result > 0;
    }

    @Override
    @Transactional
    public void increaseSales(Long productId, Integer quantity) {
        productRepository.increaseSales(productId, quantity);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        if (product.getSpecifications() != null) {
            List<ProductSpecificationDTO> specDTOs = product.getSpecifications().stream()
                    .map(spec -> {
                        ProductSpecificationDTO specDTO = new ProductSpecificationDTO();
                        BeanUtils.copyProperties(spec, specDTO);
                        return specDTO;
                    })
                    .collect(Collectors.toList());
            dto.setSpecifications(specDTOs);
        }
        return dto;
    }
}
