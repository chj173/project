package com.ex.project.service;

import com.ex.project.dto.CommentDTO;
import com.ex.project.entitiy.BoardEntity;
import com.ex.project.entitiy.CommentEntity;
import com.ex.project.repository.BoardRepository;
import com.ex.project.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    public Long save(CommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회
           부모 조회 후 있다면 변환 후 저장
        */
        Optional<BoardEntity> OptionBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if(OptionBoardEntity.isPresent()) {
            BoardEntity boardEntity = OptionBoardEntity.get();
            // DTO -> Entity
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId(); // 저장 후 id 반환
        } else {
            return null;
        }
    }

    // 댓글 목록
    // 리스트로 반환
    @Transactional // 부모 Entity를 건드리므로 추가
    public List<CommentDTO> findAll(Long boardId) {
        // board_id를 기준으로 목록을 가져오고 내림차순으로 정렬
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* EntityList -> DTOList */
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            // Entity -> DTO
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
