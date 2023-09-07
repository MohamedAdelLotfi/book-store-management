package com.digitalfactory.bookstore;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.domain.BookCategory;
import com.digitalfactory.bookstore.domain.Transaction;
import com.digitalfactory.bookstore.domain.User;
import com.digitalfactory.bookstore.repository.BookCategoryRepository;
import com.digitalfactory.bookstore.repository.BookRepository;
import com.digitalfactory.bookstore.repository.TransactionRepository;
import com.digitalfactory.bookstore.repository.UserRepository;
import com.digitalfactory.bookstore.service.enums.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;
    
    private final PasswordEncoder passwordEncoder;

    private final BookCategoryRepository bookCategoryRepository;

    private final BookRepository bookRepository;

    private final TransactionRepository transactionRepository;
    
    @Override
    public void run(String... args) {
        log.debug("initializing book category data...");
        Arrays.asList("novel", "crime", "mystery", "romantic", "horror").forEach(v -> this.bookCategoryRepository.saveAndFlush(BookCategory.builder().name(v).build()));

        log.debug("initializing book data...");
        this.bookRepository.save(Book.builder()
                .book_category(this.bookCategoryRepository.findById(1L).get())
                .name("Dracula")
                .writer_name("Bram Stoker")
                .amount(new BigDecimal(0))
                .build());
        this.bookRepository.save(Book.builder()
                .book_category(this.bookCategoryRepository.findById(2L).get())
                .name("In Cold Blood")
                .writer_name("Truman Capote")
                .amount(new BigDecimal(0))
                .build());
        this.bookRepository.save(Book.builder()
                .book_category(this.bookCategoryRepository.findById(3L).get())
                .name("Dead Simple")
                .writer_name("Peter James")
                .amount(new BigDecimal(0))
                .build());
        this.bookRepository.save(Book.builder()
                .book_category(this.bookCategoryRepository.findById(4L).get())
                .name("The Big Sleep")
                .writer_name("Raymond Chandler")
                .amount(new BigDecimal(0))
                .build());
        this.bookRepository.save(Book.builder()
                .book_category(this.bookCategoryRepository.findById(5L).get())
                .name("Beloved")
                .writer_name("Toni Morrison")
                .amount(new BigDecimal(0))
                .build());

        log.debug("initializing book user data...");
        this.users.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .address("cairo-egypt")
                .phone("01007654321")
                .civilId("29512478563923")
                .email("u@gmail.com")
                .type(UserType.CUSTOMER.getValue())
                .roles(Arrays.asList("ROLE_USER"))
                .build()
        );
        
        this.users.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .address("cairo-egypt")
                .phone("01001234567")
                .civilId("29412345678566")
                .email("m@gmail.com")
                .type(UserType.ADMIN.getValue())
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );
        
        log.debug("printing all users...");
        this.users.findAll().forEach(v -> log.debug(" User :" + v.toString()));


        log.debug("initializing transaction data...");
        this.transactionRepository.save(Transaction.builder()
                .book(this.bookRepository.findById(1L).get())
                .user(this.users.findById(1L).get())
                .return_date(Timestamp.from(Instant.now()))
                .trx_date(Timestamp.from(Instant.now()))
                .build());
        this.transactionRepository.save(Transaction.builder()
                .book(this.bookRepository.findById(2L).get())
                .user(this.users.findById(1L).get())
                .return_date(Timestamp.from(Instant.now()))
                .trx_date(Timestamp.from(Instant.now()))
                .build());
        this.transactionRepository.save(Transaction.builder()
                .book(this.bookRepository.findById(3L).get())
                .user(this.users.findById(1L).get())
                .return_date(Timestamp.from(Instant.now()))
                .trx_date(Timestamp.from(Instant.now()))
                .build());
        this.transactionRepository.save(Transaction.builder()
                .book(this.bookRepository.findById(4L).get())
                .user(this.users.findById(1L).get())
                .return_date(Timestamp.from(Instant.now()))
                .trx_date(Timestamp.from(Instant.now()))
                .build());
        this.transactionRepository.save(Transaction.builder()
                .book(this.bookRepository.findById(5L).get())
                .user(this.users.findById(1L).get())
                .return_date(Timestamp.from(Instant.now()))
                .trx_date(Timestamp.from(Instant.now()))
                .build());
    }
}
