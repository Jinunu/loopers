package com.loopers.infrastructure.order;

import com.loopers.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userId = ?1")
    Optional<Order> findByUserId(String userId);
}
