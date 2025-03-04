package bookgle.bookgle.board.service;

import bookgle.bookgle.board.domain.Board;
import bookgle.bookgle.board.dto.BoardDto;
import bookgle.bookgle.board.dto.PageDto;
import bookgle.bookgle.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public PageDto<BoardDto.ListResponse> findAll(org.springframework.data.domain.Pageable pageable) {
        Page<BoardDto.ListResponse> page = boardRepository.findAll(pageable)
                .map(BoardDto.ListResponse::fromEntity);
        return PageDto.from(page);
    }

    public BoardDto.Response findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + id));
        
        board.increaseViewCount();
        return BoardDto.Response.fromEntity(board);
    }

    @Transactional
    public BoardDto.Response save(BoardDto.Request request) {
        Board board = request.toEntity();
        Board savedBoard = boardRepository.save(board);
        return BoardDto.Response.fromEntity(savedBoard);
    }

    @Transactional
    public BoardDto.Response update(Long id, BoardDto.Request request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + id));
        
        board.update(request.getTitle(), request.getContent());
        return BoardDto.Response.fromEntity(board);
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다. ID: " + id));
        
        boardRepository.delete(board);
    }
} 