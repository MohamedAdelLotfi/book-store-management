package com.digitalfactory.bookstore.service.impl;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.domain.Transaction;
import com.digitalfactory.bookstore.exceptions.BadRequestAlertException;
import com.digitalfactory.bookstore.repository.TransactionRepository;
import com.digitalfactory.bookstore.service.BookService;
import com.digitalfactory.bookstore.service.TransactionService;
import com.digitalfactory.bookstore.service.UserService;
import com.digitalfactory.bookstore.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update transaction : {}", transaction);

        Transaction bk = transactionRepository.findById(transaction.getId()).get();
        if (bk != null) {
            bk.setBook(transaction.getBook());
            bk.setUser(transaction.getUser());
            bk.setReturn_date(transaction.getReturn_date());
            bk.setTrx_date(transaction.getTrx_date());
            return Optional.ofNullable(save(bk));
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> trxs = transactionRepository.findAll();
        return trxs.stream().collect(Collectors.toList());
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all transactions");
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get transaction : {}", id);
        return transactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    @Override
    public String borrowBook(Transaction transaction) throws BadRequestAlertException {
        String userName = SecurityUtils.getCurrentUserLogin().get();
        if (userName == null) {
            throw new BadRequestAlertException("UserNotFound", "Transaction", null);
        }
        if (transaction.getUser() == null && userName != null) {
            transaction.setUser(userService.findByUserName(userName));
        }
        if (transaction.getBook() != null && transaction.getBook().getAmount() != null) {
             if (transaction.getBook().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                 throw new BadRequestAlertException("BookNotAvailable", "Transaction", null);
             }
        }
        if (transaction.getReturn_date() == null) {
            throw new BadRequestAlertException("ToBorrowBookShouldNeedEnterReturnDate", "Transaction", null);
        }
        transaction.setTrx_date(Timestamp.from(Instant.now()));
        Transaction trx = save(transaction);
        if (trx != null && trx.getId() > 0) {
            Book book = bookService.findOne(transaction.getBook().getId()).get();
            if (book != null) {
                book.setAmount(book.getAmount().subtract(new BigDecimal(1)));
            }
            bookService.save(book);
        }
        return "Successful";
    }

    @Override
    public String returnBook(Transaction transaction) {
        String userName = SecurityUtils.getCurrentUserLogin().get();
        if (userName == null) {
            throw new BadRequestAlertException("UserNotFound", "Transaction", null);
        }
        if (transaction.getUser() == null && userName != null) {
            transaction.setUser(userService.findByUserName(userName));
        }
        if (transaction.getBook() != null && transaction.getBook().getAmount() != null) {
            if (transaction.getBook().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestAlertException("BookNotAvailable", "Transaction", null);
            }
        }
        if (transaction.getReceived_date() == null) {
            transaction.setReceived_date(Timestamp.from(Instant.now()));
        }
        transaction.setTrx_date(Timestamp.from(Instant.now()));
        Transaction trx = save(transaction);
        if (trx != null && trx.getId() > 0) {
            Book book = bookService.findOne(transaction.getBook().getId()).get();
            if (book != null) {
                book.setAmount(book.getAmount().add(new BigDecimal(1)));
            }
            bookService.save(book);
        }
        return "Successful";
    }
}
