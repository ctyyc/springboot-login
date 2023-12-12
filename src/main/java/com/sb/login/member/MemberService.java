package com.sb.login.member;

import com.sb.login.member.model.Member;
import com.sb.login.member.model.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<MemberDto> getList() {
        List<Member> memberList = this.memberRepository.findAll();
        return memberList.stream().map(MemberDto::new).toList();
    }

    public Member create(String memberName, String email, String password) {
        Member member = new Member();
        member.setMemberName(memberName);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        member.setCreateDate(LocalDateTime.now());

        this.memberRepository.save(member);

        return member;
    }
}
