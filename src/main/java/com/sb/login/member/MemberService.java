package com.sb.login.member;

import com.sb.login.config.DataNotFoundException;
import com.sb.login.member.model.Member;
import com.sb.login.member.model.MemberDto;
import com.sb.login.member.model.MemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<MemberDto> getList() {
        List<Member> memberList = this.memberRepository.findAll();
        return memberList.stream().map(MemberDto::new).toList();
    }

    public MemberDto getMember(Long id) {
        Optional<Member> memberOptional = this.memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            return new MemberDto(memberOptional.get());
        } else {
            throw new DataNotFoundException("member not found");
        }
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

    public void modify(MemberForm memberForm, Long id) {
        Optional<Member> memberOptional = this.memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.setEmail(memberForm.getEmail());
            member.setPassword(passwordEncoder.encode(memberForm.getPassword1()));
            this.memberRepository.save(member);
        } else {
            throw new DataNotFoundException("member not found");
        }
    }

    public void delete(Long id) {
        Optional<Member> memberOptional = this.memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            this.memberRepository.delete(memberOptional.get());
        } else {
            throw new DataNotFoundException("member not found");
        }
    }
}
