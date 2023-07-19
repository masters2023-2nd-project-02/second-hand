package com.secondhand.service;

import com.secondhand.exception.ProductNotFoundException;
import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.web.dto.filtercondition.ProductCategorySearchCondition;
import com.secondhand.web.dto.filtercondition.ProductSalesSearchCondition;
import com.secondhand.web.dto.filtercondition.ProductSearchCondition;
import com.secondhand.web.dto.response.MainPageCategoryResponse;
import com.secondhand.web.dto.response.MainPageResponse;
import com.secondhand.web.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final MemberService memberService;


    @Transactional
    public ProductResponse getDetailPage(long productId, long userId) {
        Product product = findById(productId);
        productRepository.countViews(productId);
        return ProductResponse.of(product.checkIsMine(userId), product);
    }

    @Transactional
    public ProductResponse isValidMinePage(long productId, long userId) {
        Product product = findById(productId);
        return ProductResponse.of(product.checkIsDetailPageMine(userId), product);
    }

    @Transactional
    public MainPageResponse getProductList(ProductSearchCondition productSearchCondition, Pageable pageable, long userId) {
        Slice<Product> page = productRepository.findAllByTowns(productSearchCondition, pageable, userId);
        List<Product> products = page.getContent();
        log.debug("products = {}", products);
        return MainPageResponse.of(products, userId);
    }

    @Transactional
    public MainPageResponse getMemberSalesProducts(ProductSalesSearchCondition condition, Pageable pageable, long userId) {
        Slice<Product> page = productRepository.findAllByStatus(condition, pageable, userId);
        List<Product> products = page.getContent();
        for (Product product : products) {
            System.out.println("product = " + product.getStatus().getValue());
        }
        log.debug("products = {}", products);
        return MainPageResponse.of(products, userId);
    }


    public MainPageCategoryResponse getLikeProductList(ProductCategorySearchCondition productSearchCondition, Pageable pageable, long userId) {
        //로그인한 유저가 좋아요 누른목록
        Member member = memberService.findMemberById(userId);
        Set<Interested> interesteds = member.getInteresteds();
        for (Interested interested : interesteds) {
            if (interested.getProduct().getCategory().getCategoryId() == 4) {
                log.debug("interested = {}", interested.getProduct().getId());
            }
        }
        List<Long> likedCategoryIds = interesteds.stream()
                .map(interested -> interested.getProduct().getCategory().getCategoryId())
                .collect(Collectors.toList());
        log.debug("likedCategoryIds ={}", likedCategoryIds);

        Slice<Product> page = productRepository.findAllByCategory(productSearchCondition, pageable, userId);
        List<Product> products = page.getContent();

        List<Long> categoryIds = products.stream()
                .map(product -> product.getCategory().getCategoryId())
                .collect(Collectors.toList());

        log.debug("categoryIds = {}", categoryIds);
        return MainPageCategoryResponse.of(products, likedCategoryIds, userId);
    }

    public Product findById(long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

}