package org.example;

import org.example.model.Category;
import org.example.model.Transaction;
import org.example.repository.CategoryRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class MyWalletServiceApplication implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public MyWalletServiceApplication(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyWalletServiceApplication.class, args);
    }

    @Override
    public void run(String...args) throws Exception {


        System.out.println("Categories: " + categoryRepository.findAll());
        System.out.printf("Transactions: " + transactionRepository.findAll());
    }
}
