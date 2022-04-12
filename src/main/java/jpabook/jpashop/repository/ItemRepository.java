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
          em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from item i", Item.class)
                .getResultList();
    }
    
}
