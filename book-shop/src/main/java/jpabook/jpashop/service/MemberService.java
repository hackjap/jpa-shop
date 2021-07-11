package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validationDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validationDuplicateMember(Member member) {
        // EXCEPTION

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) { // 이름이 존재할 경우
            throw new IllegalStateException("이미 존재하는 회원입니다.");
            // 실무에서는 DB에 UNIQUE 옵션을 적용하는 것이 맞음
        }
    }

    /**
     * 전체 회원 조희
     */
    public List<Member> findMembers() {
        return memberRepository.findALl();

    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
