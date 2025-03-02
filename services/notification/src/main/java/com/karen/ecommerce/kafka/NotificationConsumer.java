package com.karen.ecommerce.kafka;

import com.karen.ecommerce.email.EmailService;
import com.karen.ecommerce.kafka.order.OrderConfirmation;
import com.karen.ecommerce.kafka.payment.PaymentConfirmation;
import com.karen.ecommerce.notification.Notification;
import com.karen.ecommerce.notification.NotificationRepository;
import com.karen.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;

 @KafkaListener(topics = "payment-topic")
    public void consumerPaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
     repository.save(
             Notification.builder()
                     .type(NotificationType.PAYMENT_CONFIRMATION)
                     .notificationTime(LocalDateTime.now())
                     .paymentConfirmation(paymentConfirmation)
                     .build()

     );
     // send email
     var customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
     emailService.sendPaymentSuccessEmail(
             paymentConfirmation.customerEmail(),
             customerName,
             paymentConfirmation.amount(),
             paymentConfirmation.orderReference()
     );
 }
    @KafkaListener(topics = "order-topic")
    public void consumerOrderConfirmationNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        repository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationTime(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()

        );
        // send email
        var customerName = orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }

}
