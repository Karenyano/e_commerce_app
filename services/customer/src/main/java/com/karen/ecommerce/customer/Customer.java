package com.karen.ecommerce.customer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {
     @org.springframework.data.annotation.Id
    private String Id;
    private  String firstname;
    private  String lastname;
    private String email;
    private Address address;
}
