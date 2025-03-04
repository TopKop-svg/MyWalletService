package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping ("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransactions(@Valid @RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id,
                                                         @Valid
                                                         @RequestBody Transaction transactionDetails) {
        return transactionService.updateTransaction(id, transactionDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/import")
    public ResponseEntity<List<Transaction>> importTransactions(@RequestParam("file")MultipartFile file) {
        try {
            List<Transaction> importedTransactions = transactionService.importTransactionsFromCsv(file);
            return ResponseEntity.ok(importedTransactions);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
