package com.karen.ecommerce.email;


import lombok.Getter;

public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment-confirmation.html","payment successfully processed"),
    ORDER_CONFIRMATION("order-confirmation.html","order confirmation");

    @Getter
    private final String template;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }

    @Getter
    private final String subject;
}
