package com.karen.ecommerce.kafka;

import com.karen.ecommerce.customer.CustomerResponse;
import com.karen.ecommerce.order.PaymentMethod;
import com.karen.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse>products
) {
}
