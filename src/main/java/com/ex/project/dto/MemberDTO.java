package com.ex.project.dto;

import com.ex.project.entitiy.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {

    private Long id;
    private String name;
    private String email;
    private String password;

    public static MemberDTO EntityToDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setName(memberEntity.getName());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setPassword(memberEntity.getPassword());
        return memberDTO;
    }
}