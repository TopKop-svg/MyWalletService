package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;



@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double amount;

    @Column(name = "description")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @Column(name = "date", nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category cannot be null")
    private Category category;


}
