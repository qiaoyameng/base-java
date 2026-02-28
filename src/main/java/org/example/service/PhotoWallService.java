package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.example.entity.PhotoWall;
import org.example.entity.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.PhotoWallRepository;
import org.example.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoWallService {

    private final PhotoWallRepository photoWallRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PhotoWall uploadPhoto(Long customerId, Long productId, String image, String description) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId).orElse(null);
        }

        PhotoWall photo = new PhotoWall();
        photo.setCustomerId(customerId);
        photo.setCustomerNickname(customer.getNickname());
        photo.setCustomerAvatar(customer.getAvatar());
        photo.setProductId(productId);
        if (product != null) {
            photo.setProductName(product.getName());
        }
        photo.setImage(image);
        photo.setDescription(description);
        photo.setLikes(0);
        photo.setIsTop(false);
        photo.setEnabled(true);

        return photoWallRepository.save(photo);
    }

    public Page<PhotoWall> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isTop", "createTime"));
        return photoWallRepository.findByEnabled(true, pageable);
    }

    public Page<PhotoWall> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return photoWallRepository.findByCustomerId(customerId, pageable);
    }

    public Page<PhotoWall> findByProductId(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return photoWallRepository.findByProductId(productId, pageable);
    }

    public List<PhotoWall> findTopPhotos() {
        return photoWallRepository.findByEnabledAndIsTopTrue(true);
    }

    public PhotoWall findById(Long id) {
        return photoWallRepository.findById(id).orElse(null);
    }

    @Transactional
    public PhotoWall updatePhoto(Long id, String description) {
        PhotoWall photo = findById(id);
        if (photo != null) {
            if (description != null) photo.setDescription(description);
            return photoWallRepository.save(photo);
        }
        return null;
    }

    @Transactional
    public void like(Long id) {
        photoWallRepository.incrementLikes(id);
    }

    @Transactional
    public void toggleTop(Long id, Boolean isTop) {
        PhotoWall photo = findById(id);
        if (photo != null) {
            photo.setIsTop(isTop);
            photoWallRepository.save(photo);
        }
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        PhotoWall photo = findById(id);
        if (photo != null) {
            photo.setEnabled(enabled);
            photoWallRepository.save(photo);
        }
    }

    @Transactional
    public void delete(Long id) {
        photoWallRepository.deleteById(id);
    }
}
