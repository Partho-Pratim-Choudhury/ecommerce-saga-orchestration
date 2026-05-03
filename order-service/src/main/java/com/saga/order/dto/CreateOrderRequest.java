package com.saga.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateOrderRequest {
    @NotBlank
    private String customerId;

    @NotBlank
    private String productId;

    @Min(1)
    private Integer quantity;

    @DecimalMin("0.01")
    private BigDecimal totalAmount;
}
