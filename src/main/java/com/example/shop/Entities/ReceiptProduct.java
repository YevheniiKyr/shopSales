package com.example.shop.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "receipt_product")
public class ReceiptProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long id;
    @ManyToOne
    private Receipt receipt;
    @ManyToOne
    private Product product;
    private Integer quantity;
    private Double pricePerUnit;

    // Getters and setters
}
