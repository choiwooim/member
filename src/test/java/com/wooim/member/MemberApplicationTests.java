package com.wooim.member;

import com.wooim.member.domain.Member;
import com.wooim.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

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
	void find(){

		Optional<Member> member = memberRepository.findById(1);

		if(member.isPresent()){
			Member m = member.get();
			System.out.println("return :: " + m.getMbrNm());
		}

	}

}
