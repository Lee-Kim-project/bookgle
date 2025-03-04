package bookgle.bookgle.board.dto;

import bookgle.bookgle.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        private String content;

        @NotBlank(message = "작성자는 필수 입력 항목입니다.")
        @Size(min = 2, max = 20, message = "작성자는 2자 이상 20자 이하로 입력해주세요.")
        private String author;

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .author(author)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String author;
        private int viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<CommentDto.Response> comments;

        public static Response fromEntity(Board board) {
            return Response.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .author(board.getAuthor())
                    .viewCount(board.getViewCount())
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .comments(board.getComments().stream()
                            .map(CommentDto.Response::fromEntity)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListResponse {
        private Long id;
        private String title;
        private String author;
        private int viewCount;
        private int commentCount;
        private LocalDateTime createdAt;

        public static ListResponse fromEntity(Board board) {
            return ListResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .author(board.getAuthor())
                    .viewCount(board.getViewCount())
                    .commentCount(board.getComments().size())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }
} 