package com.ex.project.controller;

import com.ex.project.dto.MemberDTO;
import com.ex.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    // @RequiredArgsConstructor가 private final을 읽어 주입
    private final MemberService memberService;

    // 회원가입화면
    @GetMapping("/register")
    public String registerForm() {
        return "/member/register";
    }

    // 회원가입
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "/member/login";
    }


    // 로그인화면
    @GetMapping("/login")
    public String loginForm() {
        return "/member/login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null){
            // login success
            session.setAttribute("loginEmail", loginResult.getEmail());
            session.setAttribute("loginId", loginResult.getId());
            session.setAttribute("loginName", loginResult.getName());
            return "index";
        } else {
            // login fail
            return "/member/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 삭제
        session.invalidate();
        return "index";
    }

    // 회원목록
    @GetMapping("/list")
    public String findAll(Model model) {
        List<MemberDTO> listResult = memberService.findAll();
        model.addAttribute("listResult", listResult);
        return "/member/list";
    }

    // 회원정보수정
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        String loginEmail = (String)session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(loginEmail);
        model.addAttribute("updateMember", memberDTO);
        return "/member/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/";
    }

    // 회원정보삭제
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id, HttpSession session) {
        memberService.deleteById(id);
        session.invalidate();
        return "redirect:/";
    }

    // ajax통신 (중복 이메일 확인)
    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("email") String email) {
        System.out.println("email = " + email);
        String emailCheck = memberService.emailCheck(email);
        return emailCheck; // 사용가능 ok 아니면 null
    }
}
