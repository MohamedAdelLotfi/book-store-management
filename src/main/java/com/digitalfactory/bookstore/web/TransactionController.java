package com.digitalfactory.bookstore.web;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.domain.Transaction;
import com.digitalfactory.bookstore.exceptions.BadRequestAlertException;
import com.digitalfactory.bookstore.repository.TransactionRepository;
import com.digitalfactory.bookstore.service.TransactionService;
import com.digitalfactory.bookstore.web.response.EntityId;
import com.digitalfactory.bookstore.web.response.HeaderUtil;
import com.digitalfactory.bookstore.web.response.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class TransactionController {

    private final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private static final String ENTITY_NAME = "transaction";
    private static final String applicationName = "BookStore";

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping(
            value = "/transaction",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public EntityId<Long> createTransaction(@RequestBody Transaction transaction)
            throws URISyntaxException {
        log.debug("REST request to save transaction : {}", transaction);
        if (transaction.getId() != null) {
            throw new BadRequestAlertException("A new Transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transaction result = transactionService.save(transaction);

        EntityId<Long> entity = new EntityId<>();
        entity.setId(result.getId());
        return entity;
    }

    @PutMapping(
            value = "/transaction/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to update transaction : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transaction result = transactionService.save(transaction);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(
            value = "/transaction/{id}",
            consumes = { "application/json", "application/merge-patch+json" },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Transaction> partialUpdateTransaction(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transaction result = transactionService.save(transaction);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping(
            value = "/transaction/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    @GetMapping(
            value = "/transaction-pageable",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Page<Transaction>> getTransactionPageable(Pageable pageable) {
        log.debug("REST request to get Transaction : {}", pageable);
        Page<Transaction> transactions = transactionService.findAll(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(
            value = "/transactions",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<Transaction>> getTransactionAll() {
        log.debug("REST request to get Transaction : {}");
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping(
            value = "/customer/request-book",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createBook(@RequestBody Transaction trx)
            throws URISyntaxException {
        log.debug("REST request to request book : {}", trx);
        return transactionService.borrowBook(trx);
    }

    @PostMapping(
            value = "/customer/return-book",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public String returnBook(@RequestBody Transaction trx)
            throws URISyntaxException {
        log.debug("REST request to request book : {}", trx);
        return transactionService.returnBook(trx);
    }
}
