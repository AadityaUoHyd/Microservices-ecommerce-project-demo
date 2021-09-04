package org.aadi.ecommerce.order.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aadi.ecommerce.order.client.InventoryClient;
import org.aadi.ecommerce.order.dto.OrderDto;
import org.aadi.ecommerce.order.model.Order;
import org.aadi.ecommerce.order.repository.OrderRepository;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final StreamBridge streamBridge;
    private final ExecutorService traceableExecutorService;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {
        circuitBreakerFactory.configureExecutorService(traceableExecutorService);
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        java.util.function.Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItemsList().stream()
                .allMatch(orderLineItems -> {
                    log.info("Calling Inventory Service to fetch SkuCode {}", orderLineItems.getSkuCode());
                    return inventoryClient.checkStock(orderLineItems.getSkuCode());
                });
        boolean productsInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErrorCase());

        if (productsInStock) {
            Order order = new Order();
            order.setOrderLineItems(orderDto.getOrderLineItemsList());
            order.setOrderNumber(UUID.randomUUID().toString());

            orderRepository.save(order);
            log.info("Sending Order Details with Order Id {} to Notification Service", order.getId());
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(order.getId()).build());
            return "Your Order Placed Successfully.";
        } else {
            return "Your Order Failed - as one of the Product in your Order is out of stock.";
        }
    }

    private Boolean handleErrorCase() {
        return false;
    }
}