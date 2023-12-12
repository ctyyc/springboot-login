package com.sb.login.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberDto {
    private Long id;

    private String memberName;

    private String email;

    private LocalDateTime createDate;

    public MemberDto(Member e) {
        this.setId(e.getId());
        this.setMemberName(e.getMemberName());
        this.setEmail(e.getEmail());
        this.setCreateDate(e.getCreateDate());
    }
}
