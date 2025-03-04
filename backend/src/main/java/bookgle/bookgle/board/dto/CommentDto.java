package bookgle.bookgle.board.dto;

import bookgle.bookgle.board.domain.Board;
import bookgle.bookgle.board.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        private String content;

        @NotBlank(message = "작성자는 필수 입력 항목입니다.")
        @Size(min = 2, max = 20, message = "작성자는 2자 이상 20자 이하로 입력해주세요.")
        private String author;

        public Comment toEntity(Board board) {
            return Comment.builder()
                    .board(board)
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
        private Long boardId;
        private String content;
        private String author;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response fromEntity(Comment comment) {
            return Response.builder()
                    .id(comment.getId())
                    .boardId(comment.getBoard().getId())
                    .content(comment.getContent())
                    .author(comment.getAuthor())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
} 