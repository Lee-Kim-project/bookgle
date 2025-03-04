package bookgle.bookgle.board.controller;

import bookgle.bookgle.board.dto.BoardDto;
import bookgle.bookgle.board.dto.CommentDto;
import bookgle.bookgle.board.dto.PageDto;
import bookgle.bookgle.board.service.BoardService;
import bookgle.bookgle.board.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        PageDto<BoardDto.ListResponse> pageDto = boardService.findAll(pageable);
        model.addAttribute("page", pageDto);
        return "board/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardDto.Response board = boardService.findById(id);
        model.addAttribute("board", board);
        model.addAttribute("comment", new CommentDto.Request());
        return "board/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("board", new BoardDto.Request());
        return "board/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("board") BoardDto.Request request) {
        BoardDto.Response response = boardService.save(request);
        return "redirect:/board/" + response.getId();
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDto.Response board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("board") BoardDto.Request request) {
        boardService.update(id, request);
        return "redirect:/board/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board";
    }

    @PostMapping("/{boardId}/comments")
    public String addComment(@PathVariable Long boardId, 
                            @RequestParam("author") String author,
                            @RequestParam("content") String content) {
        CommentDto.Request request = CommentDto.Request.builder()
                .author(author)
                .content(content)
                .build();
        commentService.save(boardId, request);
        return "redirect:/board/" + boardId;
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long boardId) {
        commentService.delete(commentId);
        return "redirect:/board/" + boardId;
    }
} 