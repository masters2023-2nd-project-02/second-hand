package com.secondhand.config;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.secondhand.config.FixtureBuilderFactory.builderProduct;

public class FixtureFactory {

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
            .defaultNotNull(true)
            .build();


    public static Product buildProductWithId(long id) {

        return builderProduct()
                .set("id", id)
                .sample();
    }

    public static Product buildProductWithProductSaveRequestAndTownAndCategory(ProductSaveRequest productSaveRequest, Member member, Category category, Town town) {
        return builderProduct()
                .set("title", productSaveRequest.getTitle())
                .set("content", productSaveRequest.getContent())
                .set("price", productSaveRequest.getPrice())
                .set("category", category)
                .set("town", town)
                .set("member", member)
                .sample();
    }

    public static ProductSaveRequest createProductSaveRequest() {

        return new ProductSaveRequest(
                "감자 팝니다",
                1000000,
                "맛있는 감자",
                3L,
                1L
                );
    }
}
