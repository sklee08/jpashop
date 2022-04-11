package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩 필수
    @JoinColumn(name = "member_id")
    private Member member;

    // JPQL select o from order o; -> SQL select * from order;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // persist를 전파해줌
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();



    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 지연로딩 필수
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송

    private LocalDateTime orderDate; // 주문시간
    // order_date 로 변경해버림 (JPA 상에서)

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 ORDER , CANCEL

    // 연관관계 편입 메소드
    // 양방향 메소드

    public void setMemeber(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void setOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static void main(String[] args) {

    }
}
