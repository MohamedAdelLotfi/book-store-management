package com.digitalfactory.bookstore.service;

import com.digitalfactory.bookstore.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    Transaction save(Transaction transaction);

    /**
     * Partially updates a transaction.
     *
     * @param transaction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Transaction> partialUpdate(Transaction transaction);

    /**
     * Get all the transactions.
     *
     * @return the list of entities.
     */
    List<Transaction> findAll();

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * Get the "id" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Transaction> findOne(Long id);

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * user borrow book
     *
     * @param transaction the entity with data
     */
    String borrowBook(Transaction transaction);

    /**
     * user return book
     *
     * @param transaction the entity with data
     */
    String returnBook(Transaction transaction);
}
