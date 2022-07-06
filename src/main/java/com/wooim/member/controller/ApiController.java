package com.wooim.member.controller;


import com.wooim.config.JwtTokenProvider;
import com.wooim.member.domain.Member;
import com.wooim.member.domain.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = {"LoginController"})
@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @GetMapping(value = "/signin")
    public ResponseEntity<?> signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {

        Map<String,Object> retMap = new HashMap<>();
        String token = "";
        try{
            //validation
            if(!isValidEmail(email)){
                retMap.put("msg","이메일 형식이 올바르지 않습니다.");
                return new ResponseEntity<>(retMap, HttpStatus.OK);
            }

            Optional<Member>  member = memberRepository.findByEmail(email);
            if(member.isEmpty()){
                throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
            }
            if (!encoder.matches(password, member.get().getPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다.");
            }

            token = jwtTokenProvider.createToken(member.get().getEmail());

            retMap.put("ret",1);
            retMap.put("token",token);
            
        }catch(Exception e){
            retMap.put("ret",9999);
            retMap.put("msg",e.getMessage());
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

/*
    @PostMapping(value = "/signup")
    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    public void signup(

            @ApiParam(value = "이메일", example = "wooim.choi@gmail.com") @RequestParam String email,
            @ApiParam(value = "비밀번호", example = "test") @RequestParam String password,
            @ApiParam(value = "이름", example = "최우임") @RequestParam String name,
            @ApiParam(value = "닉네임", example = "든드라") @RequestParam(required = false) String nkNm,
            @ApiParam(value = "휴대폰번호", example = "010-1111-2222") @RequestParam(required = false) String phone

            ) throws Exception{
        System.out.println("test!!!!");
        Map<String,Object> retMap = new HashMap<>();
        try{
            //validation
//            if(!isValidEmail(email)){
//                throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
//            }



            memberRepository.save(Member.builder()
                    .email(email)
//                    .mbrPw(encoder.encode(password))
//                    .mbrNm(name)
//                    .mbrNkNm(nkNm)
//                    .phone(phone)
//                    .email(email)
                    .regDtt(LocalDate.now())
                    .modDtt(LocalDate.now())
                    //.roles(Collections.singletonList("ROLE_USER"))
                    .build());

            retMap.put("ret",1);
            retMap.put("msg","회원가입 성공");
        }catch(Exception e){
            retMap.put("ret",9999);
            retMap.put("msg",e.getMessage());
        }

        //return new ResponseEntity<>(retMap, HttpStatus.OK);
    }
*/
    @GetMapping(value = "/signup")
    @ApiOperation(value = "가입", notes = "회원가입을 진행한다.")
    public ResponseEntity<?> signup(
            @ApiParam(value = "이메일", example = "wooim.choi@gmail.com") @RequestParam String email,
            @ApiParam(value = "비밀번호", example = "test") @RequestParam String password,
            @ApiParam(value = "이름", example = "최우임") @RequestParam String name,
            @ApiParam(value = "닉네임", example = "든드라") @RequestParam(required = false) String nkNm,
            @ApiParam(value = "휴대폰번호", example = "010-1111-2222") @RequestParam(required = false) String phone
    ){
        Map<String,Object> retMap = new HashMap<>();
        try{
            //validation
            if(!isValidEmail(email)){
                throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
            }
            if(phone != null && phone.length() > 0 && !isValidMobile(phone)){
                throw new RuntimeException("핸드폰 형식이 올바르지 않습니다.");
            }

            Optional<Member>  member =  memberRepository.findByEmail(email);
            if(member.isEmpty()){
                memberRepository.save(Member.builder()
                        .email(email)
                        .mbrPw(encoder.encode(password))
                        .mbrNm(name)
                        .mbrNkNm(nkNm)
                        .phone(phone)
                        .email(email)
                        .regDtt(LocalDate.now())
                        .modDtt(LocalDate.now())
                        .build());
            }else{
                throw new RuntimeException("이미 존재하는 이메일입니다.");
            }

            retMap.put("ret",1);
            retMap.put("msg","회원가입 성공");
        }catch(Exception e){
            retMap.put("ret",9999);
            retMap.put("msg",e.getMessage());
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    @ApiOperation(value = "회원정보", notes = "내정보를 조회한다.")
    @GetMapping(value = "/myinfo")
    public ResponseEntity<?> myinfo(
            @ApiParam(value = "토큰", required = true) @RequestParam String token) {

        Map<String,Object> retMap = new HashMap<>();
        try{
            String email = jwtTokenProvider.getUserPk(token);
            Optional<Member>  member = memberRepository.findByEmail(email);

            if(!member.isEmpty()){
                retMap.put("info",member.get().toString());
            }

            retMap.put("ret",1);


        }catch(Exception e){
            retMap.put("ret",9999);
            retMap.put("msg",e.getMessage());
        }

        return new ResponseEntity<>(retMap, HttpStatus.OK);
    }

    @ApiOperation(value = "휴대전화 인증", notes = "휴대폰 번호를 인증한다.")
    @GetMapping(value = "/chkPhone")
    public ResponseEntity<?> chkPhone(
            @ApiParam(value = "휴대폰번호", required = true) @RequestParam String phone) {

        Map<String,Object> retMap = new HashMap<>();
        try{
            Optional<Member>  member = memberRepository.findByPhone(phone);

            if(member.isEmpty()){
                throw new RuntimeException("존재하지 않는 휴대폰번호 입니다.");
            }

            retMap.put("ret",1);

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

    /**
     * mobile validation
     */
    public static boolean isValidMobile(String mobile) {
        boolean err = false;
        String regex = "^\\d{3}-\\d{3,4}-\\d{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        if(m.matches()) {
            err = true;
        }
        return true;
        //return err;
    }

}