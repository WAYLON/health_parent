package com.waylon.service;

import com.waylon.pojo.Member;

public interface MemberService {
    public void add(Member member);

    public Member findByTelephone(String telephone);

    public void sendLoginSmsCode(String telephone);
}
