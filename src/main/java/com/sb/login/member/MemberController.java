package com.sb.login.member;

import com.sb.login.member.model.MemberDto;
import com.sb.login.member.model.MemberForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model) {
        List<MemberDto> memberDtoList = this.memberService.getList();
        model.addAttribute("memberList", memberDtoList);
        return "member-list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, MemberForm memberForm) {
        MemberDto memberDto = this.memberService.getMember(id);
        model.addAttribute("member", memberDto);
        return "member-detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String memberModify(MemberForm memberForm, @PathVariable("id") Long id, Principal principal) {
        MemberDto memberDto = this.memberService.getMember(id);
        if(!memberDto.getMemberName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        memberForm.setMemberName(memberDto.getMemberName());
        memberForm.setEmail(memberDto.getEmail());

        return "member-form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String memberModify(@Valid MemberForm memberForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "member-form";
        }

        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "member-form";
        }

        MemberDto memberDto = this.memberService.getMember(id);
        if (!memberDto.getMemberName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        this.memberService.modify(memberForm, id);

        return String.format("redirect:/member/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String memberDelete(Principal principal, @PathVariable("id") Long id) {
        MemberDto memberDto = this.memberService.getMember(id);
        if (!memberDto.getMemberName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.memberService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

    @GetMapping("/signup")
    public String signup(MemberForm memberForm) {
        return "signup-form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup-form";
        }

        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup-form";
        }

        try {
            memberService.create(memberForm.getMemberName(), memberForm.getEmail(), memberForm.getPassword1());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup-form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup-form";
        }

        return "redirect:/";
    }
}
