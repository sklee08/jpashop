package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 자동으로 스프링 빈에 등록
// Spring 이 훨씬 옵션들이 많아서 권장함 
@Service
@Transactional(readOnly = true) // 읽기 전용이라 읽기용 모드로 읽어서 부하 저하
@RequiredArgsConstructor // final이 있는 애만 생성자메소드 만들어줌
public class MemberService {
    
    // 필드 인젝션 
    // 스프링에서 자동으로 가져와줌
    // autowired 대신
    private final MemberRepository memberRepository;

    // autowired 로 하면 바꿀수가 없음

    // 회원 가입
    @Transactional
    public Long join(Member member){

        //validation 중복 회원 검증로직
        validateDuplicateMember(member);

        memberRepository.save(member);

        // 값이 항상 보장되어 있음 Key - Value 관계라서 처음에 Key인 Id 를 넣고 시작함
        return member.getId();
    }
    
    private void validateDuplicateMember (Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        }
    }

    // 회원 전체 조회
    // 읽기 전용이라 읽기용 모드로 읽어서 부하 저하
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 id 로 한명 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
