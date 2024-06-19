package com.karen.ecommerce.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody  @Valid CustomerRequest request){
        return ResponseEntity.ok(customerService.createCustomer(request));
    }
    @PutMapping("/update")
    public ResponseEntity<Void> updateCustomer(@RequestBody  @Valid CustomerRequest request){
         customerService.updateCustomer(request);

        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }
    @GetMapping("/{customer-id}")
    public ResponseEntity <CustomerResponse> findById(@PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(customerService.findById(customerId));
    }
    @DeleteMapping("/{customer-id}")
    public ResponseEntity <Void> delete(@PathVariable("customer-id") String customerId){
        customerService.deleteCustomer(customerId);
                return ResponseEntity.ok().build();
    }
}
