package com.karen.ecommerce.payment;

import com.karen.ecommerce.customer.CustomerResponse;
import com.karen.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
