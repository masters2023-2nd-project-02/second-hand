package com.secondhand.service;

import com.secondhand.domain.categorie.Category;
import com.secondhand.domain.exception.NotUserMineProductException;
import com.secondhand.domain.exception.ProductNotFoundException;
import com.secondhand.domain.image.Image;
import com.secondhand.domain.image.ImageRepository;
import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.interested.InterestedRepository;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.repository.ProductRepository;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import com.secondhand.web.dto.requset.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InterestedRepository interestedRepository;
    private final CategoryService categoryService;
    private final TownService townService;
    private final MemberService memberService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Transactional
    public Long save(long userId, ProductSaveRequest requestInfo) {
        Category category = categoryService.findById(requestInfo.getCategoryId());
        Town town = townService.findById(requestInfo.getTownId());
        Member member = memberService.findMemberById(userId);
        Product product = Product.create(requestInfo, member, category, town);
        Product saveProduct = productRepository.save(product);
        List<String> imageUrls = imageService.uploadImageList(requestInfo.getProductImages()); //s3에 이미지 올라감

        saveProduct.updateThumbnail(imageUrls.get(0));
        for (String url : imageUrls) {
            Image image = new Image(url, saveProduct);
            imageRepository.save(image);
        }
        return saveProduct.getId();
    }

    @Transactional
    public void update(long productId, ProductUpdateRequest updateRequest, long userId) {
        Category category = categoryService.findById(updateRequest.getCategoryId());
        Town town = townService.findById(updateRequest.getTownId());
        Product product = findById(productId);
        checkIsMine(userId, product.getMember().getId());
        product.update(updateRequest, category, town);
        log.debug("product = {}", product);
    }

    @Transactional
    public void changeLike(long productId, long userId, boolean likeRequest) {
        Member member = memberService.findMemberById(userId);
        Product product = findById(productId);
        //   checkIsMine(member.getId(), product.getMember().getId());
        Optional<Interested> findInterested = interestedRepository.findByProductIdAndMemberId(productId, member.getId());
        log.debug("좋아요 = {}", findInterested);
        if (findInterested.isPresent()) {
            interestedRepository.delete(findInterested.get());
            log.debug("product.getInteresteds() = {}", product.getInteresteds());
            return;
        }
        Interested interested = Interested.create(member, product, likeRequest);
        Interested save = interestedRepository.save(interested);
        log.debug("만들어진 좋아요 상품 번호 = {}", save.getProduct().getId());
        log.debug("만들어진 좋아요 회원 번호 = {}", save.getMember().getId());
        product.updateInterested(save);
    }


    @Transactional
    public void changeStatus(long productId, long userId, Integer statusRequest) {
        Product product = findById(productId);
        Long memberId = product.getMember().getId();
        if (memberId == userId) {
            product.updateStatus(statusRequest);
            return;
        }
        throw new NotUserMineProductException();
    }


    //TODO 굳이 필요없어보임


    @Transactional
    public void delete(long userId, long productId) {
        Product product = findById(productId);
        checkIsMine(userId, product.getMember().getId());
        if (product.getMember().getId() == userId) {
            productRepository.delete(product);
        }
    }

    private boolean checkIsMine(long userId, long product) {
        if (product == userId) {
            return true;
        }
        throw new NotUserMineProductException();
    }

    public Product findById(long productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }
}
