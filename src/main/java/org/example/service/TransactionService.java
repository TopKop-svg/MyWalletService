package org.example.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.Category;
import org.example.model.Transaction;
import org.example.repository.CategoryRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setAmount(transactionDetails.getAmount());
                    transaction.setDescription(transactionDetails.getDescription());
                    transaction.setDate(transactionDetails.getDate());
                    transaction.setCategory(transactionDetails.getCategory());
                    return transactionRepository.save(transaction);
                });
    }

    public boolean deleteTransaction(Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transactionRepository.delete(transaction);
                    return true;
                })
                .orElse(false);
    }

    public List<Transaction> importTransactionsFromCsv(MultipartFile file) throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                Transaction transaction = new Transaction();
                transaction.setAmount(Double.parseDouble(csvRecord.get("amount")));
                transaction.setDescription(csvRecord.get("description"));
                transaction.setDate(LocalDate.parse(csvRecord.get("date")));

                Long categoryId = Long.parseLong(csvRecord.get("category_id"));
                Optional<Category> category = categoryRepository.findById(categoryId);
                if (category.isPresent()) {
                    transaction.setCategory(category.get());
                } else {
                    throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
                }
                transactions.add(transaction);
            }
            return transactionRepository.saveAll(transactions);
        } catch (Exception ex) {
            throw new Exception("Failed to import CSV: " + ex.getMessage(), ex);
        }
    }

}
