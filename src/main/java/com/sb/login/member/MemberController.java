package com.sb.login.member;

import com.sb.login.member.model.MemberDto;
import com.sb.login.member.model.MemberForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
