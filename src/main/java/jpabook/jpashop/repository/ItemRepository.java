package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 등록
    public void save(Item item){
        if(item.getId() == null){
            // 신규 등록
            em.persist(item);
        } else{
            // 이미 등록된 값을 Update 한다고 생각
            // 준영속상태의 엔티티 -> 영속상태로 만듦.
            // 병합시 모든 필드를 변경해버림 (값이 없다면 NULL로 변경할 수도 있음)
          em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i ", Item.class)
                .getResultList();
    }
    
}
