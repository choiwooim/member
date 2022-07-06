
# SpringSecurity Api with Swagger3.0

## Description
- Spring Boot + mysql + jpa 를 이용하여 회원가입 api를 개발하였습니다.
- Swagger를 통하여 api 정보를 관리 및 테스트가 가능합니다.
  (http://localhost:8080/swaagger-ui/index.html)
- 토큰은 jwt토큰을 사용하였으며, 이메일 정보가 포함됩니다.

## Function
1.회원가입(/api/signup)
- 필수값 : 이름, 휴대폰번호, 이메일, 비밀번호
- 유효성 체크 : 이메일, 휴대폰 
- 이메일은 중복체크를 통하여 아이디로 활용

2.로그인(/api/signin)
- 필수값 : 이메일, 비밀번호
- 로그인 성공 시 jwt 토큰을 발행합니다. 
- 로그인 이후 "token filter"를 통하여 토큰정보를 체크합니다. 

3.내정보(/myinfo)
- 필수값 : 토큰
- request header의 토큰값을 통하여 내정보를 호출하게 하려고 했으나, 
테스트용으로 토큰값을 전달받아 내정보를 출력

4.휴대폰번호 체크(/chkPhone)
- 회원가입 및 비밀번호 변경에 사용하기 위한 함수입니다. 
- 기존에 저장 된 휴대폰번호가 있는지 체크합니다.

5.비밀번호 변경(/modifyPw)
- 필수값 : 이메일, 비밀번호
- 휴대폰번호 체크 후 비밀번호 변경 api를 통해 비밀번호를 변경합니다.