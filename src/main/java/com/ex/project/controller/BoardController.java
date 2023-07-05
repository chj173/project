package com.ex.project.controller;

import com.ex.project.dto.BoardDTO;
import com.ex.project.dto.CommentDTO;
import com.ex.project.service.BoardService;
import com.ex.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    // 전체글 + 페이징처리 , 검색기능
    @GetMapping("/board")
    public String board(Model model, @PageableDefault(page = 1) Pageable pageable, String searchKeyword) {

        Page<BoardDTO> boardDTOList;

        if (searchKeyword == null) {
            // 전체 게시판을 가져옴
            boardDTOList = boardService.findAll(pageable);
        } else {
            // 검색한 게시판을 가져옴
            boardDTOList = boardService.boardSearchList(searchKeyword, pageable);
        }

        // 페이징처리
        int blockLimit = 10; // 페이지 번호 갯수
        // pageable.getPageNumber() 현재페이지 (page=1이므로 1이 기본값)
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit ))) - 1) * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1,  boardDTOList.getTotalPages());

        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/board/board";
    }
    
    // 글작성
    @GetMapping("/save")
    public String saveForm() {
        return "/board/board_save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/board";
    }
    
    // 글조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {
        /*
            게시글 조회시 조회수 하나 올리고
            게시글 데이터를 가져와서 출력
        */
        boardService.updateHits(id); // 조회수 +1
        BoardDTO boardDTO = boardService.findById(id);
        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);

        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber()); // 페이지값 넘겨줌

        return "/board/board_detail";
    }

    // 글수정
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate",boardDTO);
        return "/board/board_update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "redirect:/board/board";
    }

    // 글삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/board";
    }
}
