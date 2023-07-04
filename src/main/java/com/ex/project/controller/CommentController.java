package com.ex.project.controller;

import com.ex.project.dto.CommentDTO;
import com.ex.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity commentWrite(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("------------------------------");
        System.out.println("commentDTO = " + commentDTO);
        System.out.println("------------------------------");

        Long saveResult = commentService.save(commentDTO); // 반환 된 id값
        if (saveResult != null) {
            // 가져온 id값 있을 경우 ( 작성 성공 )
            // 댓글 목록 : 해당 게시글(boardId)의 댓글 전체를 가져옴
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시물이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
