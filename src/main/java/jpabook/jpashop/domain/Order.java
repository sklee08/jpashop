package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    
    // 생성 메소드

    // 오더 생성
    public static Order createOrder(Member member , Delivery delivery, OrderItem... orderItems ){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직

    // 주문 취소
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다. ");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }


    // 전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;

        for(OrderItem orderItem : this.orderItems){
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }
    
}
