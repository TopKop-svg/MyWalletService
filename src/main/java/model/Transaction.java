package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Locale;


@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    private double amount;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Locale.Category category;


}
