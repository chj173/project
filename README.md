# project
JPA를 이용한 회원가입, 게시판

# 개발환경

1. IDE: IntelliJ IDEA Community
2. Spring Boot 2.7.13
3. Java Version - 11.0.2
4. JPA , Web, thymeleaf , Lombok , MySQL8.0.33

# 페이지

## 메인페이지 (index)
   - 로그인 (member/login)
   - 회원가입 (member/register)
   - 회원목록 (member/list)
   - 게시판 (board/board)
     <br><br>
   1. 로그인 페이지 (login)
      - 이메일
      - 비밀번호
        <br><br>
   2. 회원가입 (register)
      - 이름
      - 이메일
      - 비밀번호
        <br><br>
   3. 회원목록 (list)
      - 회원번호
      - 이름
      - 이메일
      - 비밀번호

## 게시판 (board)
- 기능
    1. 글목록 (board/board)
        - 페이징처리
        - 검색기능
    2. 글작성 (board/save)
    3. 글조회 (board/{id})
        - 댓글기능
    4. 글수정 (board/update/{id})
    5. 글삭제 (board/delete/{id})