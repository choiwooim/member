package com.wooim.member.controller;
import com.wooim.config.JwtTokenProvider;
import com.wooim.member.domain.Member;
import com.wooim.member.domain.MemberRepository;
import com.wooim.service.CustomUserDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = {"회원가입"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @GetMapping(value = "/signin")
    public ResponseEntity<?> signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "휴대폰번호", required = false) @RequestParam String phone) {

        Map<String,Object> retMap = new HashMap<>();

        String token = "";

        //validation
        if(!isValidEmail(email)){
            retMap.put("ret",9999);
            retMap.put("msg","이메일 형식이 올바르지 않습니다.");
            return new ResponseEntity<>(retMap,HttpStatus.OK);
        }

        Member user = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        if (!encoder.matches(password, user.getPassword())) {
            // matches : 평문, 암호문 패스워드 비교 후 boolean 결과 return
            throw new RuntimeException();
        }

        token = jwtTokenProvider.createToken(user.getEmail());

        return new ResponseEntity<>(retMap, HttpStatus.OK);
       // return  token;
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @GetMapping(value = "/signup")
    public String signup(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "이름", required = true) @RequestParam String name,
            @ApiParam(value = "닉네임", required = false) @RequestParam String nkNm,
            @ApiParam(value = "휴대폰번호", required = false) @RequestParam String phone
            ) {

        memberRepository.save(Member.builder()
                .email(email)
                .mbrPw(encoder.encode(password))
                .mbrNm(name)
                .mbrNkNm(nkNm)
                .phone(phone)
                .email(email)
                .regDtt(LocalDate.now())
                .modDtt(LocalDate.now())
                //.roles(Collections.singletonList("ROLE_USER"))
                .build());

        return "suc";
    }

    /**
     * email validation
     */
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }

}