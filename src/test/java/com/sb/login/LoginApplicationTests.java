package com.sb.login;

import com.sb.login.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginApplicationTests {

	@Autowired
	private MemberService memberService;

	@Test
	void testJpa() {
//		for (int i = 1; i <= 300; i++) {
//			String subject = String.format("테스트 데이터입니다:[%03d]", i);
//			String content = "내용...";
//			this.memberService.create(subject, content);
//		}
	}
}
