package com.karen.ecommerce.order;

import com.karen.ecommerce.customer.CustomerClient;
import com.karen.ecommerce.exception.BusinessException;
import com.karen.ecommerce.kafka.OrderConfirmation;
import com.karen.ecommerce.kafka.OrderProducer;
import com.karen.ecommerce.orderline.OrderLineRequest;
import com.karen.ecommerce.orderline.OrderLineService;
import com.karen.ecommerce.payment.PaymentClient;
import com.karen.ecommerce.payment.PaymentRequest;
import com.karen.ecommerce.product.ProductClient;
import com.karen.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Customer not found"));
        var purchasedProducts = productClient.purchaseProducts(request.products());
        //persist order
        var order = this.repository.save(mapper.toOrder(request));
        //persist order lines
        for (PurchaseRequest purchaseRequest: request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    ));
        }
        // start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        //sending the order confirmation  -->kafka
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts

                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {

        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

    }
}
