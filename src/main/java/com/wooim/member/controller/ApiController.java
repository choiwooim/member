package com.wooim.member.controller;
import com.wooim.config.JwtTokenProvider;
import com.wooim.member.domain.Member;
import com.wooim.member.domain.MemberRepository;
import com.wooim.service.CustomUserDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = {"회원가입 및 로그인"})
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
    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> signup(
            /*
            @ApiParam(value = "이메일", example = "wooim.choi@gmail.com") @RequestParam String email,
            @ApiParam(value = "비밀번호", example = "test") @RequestParam String password,
            @ApiParam(value = "이름", example = "최우임") @RequestParam String name,
            @ApiParam(value = "닉네임", example = "든드라") @RequestParam(required = false) String nkNm,
            @ApiParam(value = "휴대폰번호", example = "010-1111-2222") @RequestParam(required = false) String phone,*/
            @RequestBody Member m
            ) {
        Map<String,Object> retMap = new HashMap<>();
        try{
            //validation
            if(!isValidEmail(m.getEmail())){
                throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
            }
            /*
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
                                */
            retMap.put("ret",1);
            retMap.put("msg","회원가입 성공");
        }catch(Exception e){
            retMap.put("ret",9999);
            retMap.put("msg",e.getMessage());
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
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