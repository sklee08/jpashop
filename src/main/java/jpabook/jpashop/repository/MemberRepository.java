package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// 기본적인 EntityManager 를 통해서 기본적인 Repository 가져오는 메소드
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // save = persist
    public void save(Member member){
        em.persist(member);
    }

    // class 와 key 값을 넘기면 반환
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    // list  조회
    // qlString -> Entity 객체에 대한 조회 ( 방식이 jpql )
    // from 의 대상이 Table 이 아닌 객체
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).
                getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",
                Member.class).setParameter("name", name).getResultList();
    }

    
}
