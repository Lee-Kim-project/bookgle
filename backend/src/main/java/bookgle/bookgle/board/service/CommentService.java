package bookgle.bookgle.board.service;

import bookgle.bookgle.board.domain.Board;
import bookgle.bookgle.board.domain.Comment;
import bookgle.bookgle.board.dto.CommentDto;
import bookgle.bookgle.board.repository.BoardRepository;
import bookgle.bookgle.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentDto.Response save(Long boardId, CommentDto.Request request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + boardId));
        
        Comment comment = request.toEntity(board);
        Comment savedComment = commentRepository.save(comment);
        return CommentDto.Response.fromEntity(savedComment);
    }

    @Transactional
    public CommentDto.Response update(Long commentId, CommentDto.Request request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        comment.update(request.getContent());
        return CommentDto.Response.fromEntity(comment);
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        commentRepository.delete(comment);
    }
} 