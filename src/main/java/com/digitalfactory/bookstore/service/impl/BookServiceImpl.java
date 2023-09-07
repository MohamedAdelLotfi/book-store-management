package com.digitalfactory.bookstore.service.impl;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.repository.BookRepository;
import com.digitalfactory.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;


    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Override
    public Book save(Book book) {
        log.debug("Request to save book : {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> partialUpdate(Book book) {
        log.debug("Request to partially update book : {}", book);

        Book bk = bookRepository.findById(book.getId()).get();
        if (bk != null) {
            bk.setBook_category(book.getBook_category());
            bk.setName(book.getName());
            bk.setAmount(book.getAmount());
            bk.setWriter_name(book.getWriter_name());
            return Optional.ofNullable(save(bk));
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream().collect(Collectors.toList());
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all books");
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get book : {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> books = findAll();
        books = books
                .stream()
                .filter(book->book.getAmount().compareTo(BigDecimal.ZERO)>0)
                .collect(Collectors.toList());
        return books;
    }

    @Override
    public List<Book> findAvailableBooksByCategory(Long book_category_id) {
        List<Book> books = bookRepository.findAllByBook_category_Id(book_category_id);
        books = books
                .stream()
                .filter(book->book.getAmount().compareTo(BigDecimal.ZERO)>0)
                .collect(Collectors.toList());
        return books;
    }
}
