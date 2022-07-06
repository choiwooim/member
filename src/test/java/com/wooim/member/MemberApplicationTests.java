package com.wooim.member;

import com.wooim.member.domain.Member;
import com.wooim.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class MemberApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder; // 비밀번호 암호화

	MemberApplicationTests(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Test
	void save() {

		Member member = Member.builder()
				.mbrNkNm("든드라")
				.mbrNm("최우임")
				.email("wooim.choi@gmail.com")
				.phone("01053575406")
				.mbrPw(passwordEncoder.encode("1234"))
				.regDtt(LocalDate.now())
				.modDtt(LocalDate.now())
				.build();
		memberRepository.save(member);
	}

	@Test
	void phone(){
		System.out.println("test!~!~!~");
		boolean err = false;
		String regex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("01053575406");
		if(m.matches()) {
			err = true;
		}
		System.out.println(m.matches());

	}

}
