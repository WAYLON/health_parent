package com.waylon.service;

import com.waylon.pojo.Member;

import java.util.List;

public interface MemberService {
    public void add(Member member);

    public Member findByTelephone(String telephone);

    public void sendLoginSmsCode(String telephone);

    public List<Integer> findMemberCountByMonth(List<String> month);
}
