package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.example.entity.Share;
import org.example.repository.CustomerRepository;
import org.example.repository.ShareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final ShareRepository shareRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Share createShare(Long customerId, String title, String content, String images, Long productId, String tags) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Share share = new Share();
        share.setCustomerId(customerId);
        share.setCustomerNickname(customer.getNickname());
        share.setCustomerAvatar(customer.getAvatar());
        share.setTitle(title);
        share.setContent(content);
        share.setImages(images);
        share.setProductId(productId);
        share.setTags(tags);
        share.setLikes(0);
        share.setViews(0);
        share.setIsTop(false);
        share.setEnabled(true);

        return shareRepository.save(share);
    }

    public Page<Share> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isTop", "createTime"));
        return shareRepository.findByEnabled(true, pageable);
    }

    public Page<Share> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return shareRepository.findByCustomerId(customerId, pageable);
    }

    public Page<Share> findByProductId(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return shareRepository.findByProductId(productId, pageable);
    }

    public List<Share> findTopShares() {
        return shareRepository.findByEnabledAndIsTopTrue(true);
    }

    public List<Share> findByTag(String tag) {
        return shareRepository.findByTagsContainingAndEnabled(tag, true);
    }

    public Share findById(Long id) {
        return shareRepository.findById(id).orElse(null);
    }

    @Transactional
    public Share updateShare(Long id, String title, String content, String images, String tags) {
        Share share = findById(id);
        if (share != null) {
            if (title != null) share.setTitle(title);
            if (content != null) share.setContent(content);
            if (images != null) share.setImages(images);
            if (tags != null) share.setTags(tags);
            return shareRepository.save(share);
        }
        return null;
    }

    @Transactional
    public void like(Long id) {
        shareRepository.incrementLikes(id);
    }

    @Transactional
    public void view(Long id) {
        shareRepository.incrementViews(id);
    }

    @Transactional
    public void toggleTop(Long id, Boolean isTop) {
        Share share = findById(id);
        if (share != null) {
            share.setIsTop(isTop);
            shareRepository.save(share);
        }
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        Share share = findById(id);
        if (share != null) {
            share.setEnabled(enabled);
            shareRepository.save(share);
        }
    }

    @Transactional
    public void delete(Long id) {
        shareRepository.deleteById(id);
    }
}
