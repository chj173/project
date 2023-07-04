package com.ex.project.dto;

import com.ex.project.entitiy.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    // (댓글) id, 작성자, 내용, 글번호, 생성시간
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        /*
            부모 Entity에서 id값을 가져옴
            부모에서 가져오기때문에 Service에 @Transactional 추가
         */
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId());

        return commentDTO;
    }
}
