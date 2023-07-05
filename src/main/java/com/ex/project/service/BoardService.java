package com.ex.project.service;

import com.ex.project.dto.BoardDTO;
import com.ex.project.entitiy.BoardEntity;
import com.ex.project.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 글작성
    public void save(BoardDTO boardDTO) {
        // DTO -> Entity
        BoardEntity boardEntity = BoardEntity.DTOtoEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 글목록 + 페이징처리
    public Page<BoardDTO> findAll(Pageable pageable) {
        // 페이징처리                               // PageRequest.of(page의 기본값 0)
        int page = pageable.getPageNumber() - 1; // 1페이지 요청하면 0이여야함
        int size = 10;
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        // 글번호, 작성자, 제목, 조회수, 작성일 ( DTO에 생성자 추가 )
        // map(Entity -> boardDTO)로 변환 시 boardEntities 메서드 기능(Page기능) 가져옴
        Page<BoardDTO> boardDTOS =
                boardEntities.map(board -> new BoardDTO(
                        board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));

        return boardDTOS;

//        글목록 ( list로 가져올 때 )
//        Entity -> DTO
//        List<BoardEntity> boardEntityList = boardRepository.findAll();
//        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for (BoardEntity boardEntity : boardEntityList) {
//            boardDTOList.add(BoardDTO.EntityToDTO(boardEntity));
//        }
//        return boardDTOList;
    }

    // 조회수 증가
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // id를 이용한 정보조회
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.EntityToDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    // 글수정
    public BoardDTO update(BoardDTO boardDTO) {
        // DTO -> Entity (DB값 변경)
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    // 글삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
    
    // 글제목으로 검색
    public Page<BoardDTO> boardSearchList(String searchKeyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int size = 10;

        // Entity -> DTO
        Page<BoardEntity> byBoardTitleContaining =
                boardRepository.findByBoardTitleContaining(searchKeyword,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id")));

        Page<BoardDTO> boardDTOS =
                byBoardTitleContaining.map(board -> new BoardDTO(
                        board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                ));
        return boardDTOS;
    }
}