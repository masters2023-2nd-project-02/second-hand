package com.secondhand.domain.board;

import com.secondhand.dto.BoardsDTOResponse;
import com.secondhand.util.Message;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.secondhand.domain.board.Status.SELL;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/boards")
public class BoardController {

    @Operation(
            summary = "메인 페이지 상품 리스트",
            tags = "board",
            description = "사용자는 상품 리스트를 볼 수 있다."
    )
    @GetMapping("/{townId}/{pageNum}")
    public ResponseEntity<Message> allProductList(@PathVariable long townId, @PathVariable long pageNum) {

        //TODO create는 DTO 에서 해준다.
        BoardsDTOResponse boardsListResponse = BoardsDTOResponse.builder()
                .title("파랑 선풍기")
                .town("역삼 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();

        BoardsDTOResponse boardsListResponse2 = BoardsDTOResponse.builder()
                .title("빨강 선풍기")
                .town("강남 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();

        BoardsDTOResponse boardsListResponse3 = BoardsDTOResponse.builder()
                .title("노랑 선풍기")
                .town("대치 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();

        BoardsDTOResponse boardsListResponse4 = BoardsDTOResponse.builder()
                .title("초록 선풍기")
                .town("역삼 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();
        List<BoardsDTOResponse> boardsDTOResponseList = new ArrayList<>();
        boardsDTOResponseList.add(boardsListResponse);
        boardsDTOResponseList.add(boardsListResponse2);
        boardsDTOResponseList.add(boardsListResponse3);
        boardsDTOResponseList.add(boardsListResponse4);


        if (boardsListResponse == null) {
            Message message = Message.builder()
                    .success(false)
                    .message("실패")
                    .apiStatus(20000)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .data(boardsDTOResponseList)
                    .build();
            return new ResponseEntity<>(message, null, HttpStatus.NOT_FOUND);
        }
        Message message = Message.builder()
                .success(true)
                .message("")
                .apiStatus(20000)
                .httpStatus(HttpStatus.OK)
                .data(boardsDTOResponseList)
                .build();
        return new ResponseEntity<>(message, null, HttpStatus.OK);
    }

    @Operation(
            summary = "단일 상품 리스트",
            tags = "board",
            description = "사용자는 단일 상품을 볼 수 있다."
    )
    @GetMapping("/{productId}")
    public ResponseEntity<Message> productDetail(@PathVariable long productId) {

        //TODO create는 DTO 에서 해준다.
        BoardsDTOResponse boardsListResponse = BoardsDTOResponse.builder()
                .title("파랑 선풍기")
                .town("역삼 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();

        Message message = Message.builder()
                .success(true)
                .message("")
                .apiStatus(20000)
                .httpStatus(HttpStatus.OK)
                .data(boardsListResponse)
                .build();

        return new ResponseEntity<>(message, null, HttpStatus.OK);
    }

    @Operation(
            summary = "상품 삭제",
            tags = "board",
            description = "사용자는 단일 상품 삭제 가능합니다.."
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<Message> deleteProduct(@PathVariable long productId) {

        //TODO create는 DTO 에서 해준다.
        BoardsDTOResponse boardsListResponse = BoardsDTOResponse.builder()
                .title("파랑 선풍기")
                .town("역삼 1동")
                .createdAt("2시간 전")
                .price("24,500원")
                .img("이미지")
                .status(SELL)
                .countInfo(new CountInfo(1L, 2L))
                .build();

        Message message = Message.builder()
                .success(true)
                .message("")
                .apiStatus(20000)
                .httpStatus(HttpStatus.OK)
                .data(boardsListResponse)
                .build();

        return new ResponseEntity<>(message, null, HttpStatus.OK);
    }
}