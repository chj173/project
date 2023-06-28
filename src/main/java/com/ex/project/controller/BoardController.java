package com.ex.project.controller;

import com.ex.project.dto.BoardDTO;
import com.ex.project.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 전체글
    @GetMapping("/board")
    public String board(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
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
    public String findById(@PathVariable Long id, Model model) {
        /*
            게시글 조회시 조회수 하나 올리고
            게시글 데이터를 가져와서 출력
        */
        boardService.updateHits(id); // 조회수 +1
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardDetail", boardDTO);
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
