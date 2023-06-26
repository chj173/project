package com.ex.project.service;

import com.ex.project.dto.MemberDTO;
import com.ex.project.entitiy.MemberEntity;
import com.ex.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    // 회원가입
    public void save(MemberDTO memberDTO) {
        // DTO -> Entity 변환
        MemberEntity memberEntity = MemberEntity.DTOtoEntitiy(memberDTO);
        memberRepository.save(memberEntity);
    }

    // 로그인
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byEmail = memberRepository.findByEmail(memberDTO.getEmail());
        if (byEmail.isPresent()) {
            // 메일이 있다면
            MemberEntity memberEntity = byEmail.get();
            if (memberEntity.getPassword().equals(memberDTO.getPassword())) {
                // Entity -> DTO 변환
                MemberDTO dto = MemberDTO.EntityToDTO(memberEntity);
                return dto;
            } else {
                return null;
            }
        } else {
            // 메일이 없다면
            return null;
        }
    }

    // 회원목록 전체조회
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        // Entity -> DTO
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.EntityToDTO(memberEntity));
        }
        return memberDTOList;
    }

    // 회원정보출력
    // 아이디가 Repository에 존재할 때
    public MemberDTO updateForm(String loginEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByEmail(loginEmail);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.EntityToDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    // 회원정보수정
    public void update(MemberDTO memberDTO) {
        // DTO -> Entity
        MemberEntity memberEntity = MemberEntity.DTOupdateEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    // 회원정보삭제
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // ajax 이메일 체크
    public String emailCheck(String email) {
        Optional<MemberEntity> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            // 조회결과 있으면 null
            return null;
        } else {
            // 조회결과 없으면 ok
            return "ok";
        }
    }
}