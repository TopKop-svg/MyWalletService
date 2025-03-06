package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transaction API", description = "Operations for managing transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get all transactions", description = "Returns a list of all ")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction")
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Operation(summary = "Create a new transactions", description = "Creates a new transaction")
    @ApiResponse(responseCode = "200", description = "Transaction created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    @PostMapping
    public ResponseEntity<Transaction> createTransactions(@Valid @RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @Operation(summary = "Update an exising transactions", description = "Updates the transaction with the given ID")
    @ApiResponse(responseCode = "200", description = "Transaction updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id,
                                                         @Valid
                                                         @RequestBody Transaction transactionDetails) {
        return transactionService.updateTransaction(id, transactionDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a transaction", description = "Delete the transaction")
    @ApiResponse(responseCode = "200", description = "Transaction updated successfully")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Import transactions from CSV", description = "Uploads a CSV file to import transactions")
    @ApiResponse(responseCode = "200", description = "Transaction imported successfully")
    @ApiResponse(responseCode = "400", description = "Invalid CSV file or data")
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
