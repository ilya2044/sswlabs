package com.example.demo.controller.customer;

import com.example.demo.dto.customer.AddressResponse;
import com.example.demo.dto.customer.CustomerResponse;
import com.example.demo.entity.customer.CustomerEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Endpoints for customer management")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserRepository userRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username"); // или "sub", если username хранится там
        log.info("Username from token: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        List<CustomerEntity> customers = customerService.getAllAccessibleCustomers(user);
        List<CustomerResponse> dtos = customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dtos);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id,
                                                            @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        String username = jwt.getClaimAsString("preferred_username"); // или "sub", если username хранится там
        log.info("Username from token: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        CustomerEntity customer = customerService.getCustomerByIdIfAuthorized(id, user);
        CustomerResponse response = convertToDTO(customer);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerEntity customer) {
        CustomerEntity saved = customerService.saveCustomer(customer);
        CustomerResponse response = convertToDTO(saved);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponse convertToDTO(CustomerEntity customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        if (customer.getAddress() != null) {
            response.setAddress(customer.getAddress());
        }
        return response;
    }
}