package com.secondhand.application.image;

import com.secondhand.domain.image.ImageFile;
import com.secondhand.domain.image.S3Uploader;
import com.secondhand.service.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("단위 테스트 - 이미지")
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @Mock
    private S3Uploader s3Uploader;

    @InjectMocks
    private ImageService imageService;

    @DisplayName("이미지 파일이 주어지면 이미지 업로드에 성공한다.")
    @Test
    void givenMultipartFile_thenSuccess() {
        // given
        var mockMultipartFile = createMockMultipartFile("test.png", MediaType.IMAGE_PNG_VALUE);

        given(s3Uploader.uploadImageFile(any(ImageFile.class))).willReturn("url");

        // when & then
        assertThatCode(() -> imageService.upload(mockMultipartFile)).doesNotThrowAnyException();
    }

    @DisplayName("여러 개의 이미지 파일이 주어지면 이미지 업로드에 성공한다.")
    @Test
    void givenMultipleMultipartFile_thenSuccess() {
        // given
        List<MultipartFile> images = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            images.add(createMockMultipartFile("test.png", MediaType.IMAGE_PNG_VALUE));
        }

        given(s3Uploader.uploadImageFiles(anyList())).willReturn(List.of("url1", "url2", "url3", "url", "url5"));

        // when & then
        assertThatCode(() -> imageService.uploadImageList(images)).doesNotThrowAnyException();
    }

    private MockMultipartFile createMockMultipartFile(String fileName, String extension) {
        return new MockMultipartFile(
                "test-image",
                fileName,
                extension,
                "imageBytes".getBytes(StandardCharsets.UTF_8)
        );
    }
}
