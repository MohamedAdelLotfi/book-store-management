package com.digitalfactory.bookstore.service.impl;

import com.digitalfactory.bookstore.domain.BookCategory;
import com.digitalfactory.bookstore.repository.BookCategoryRepository;
import com.digitalfactory.bookstore.service.BookCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookCategoryServiceImpl implements BookCategoryService {

    private final Logger log = LoggerFactory.getLogger(BookCategoryServiceImpl.class);

    private final BookCategoryRepository bookCategoryRepository;


    public BookCategoryServiceImpl(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }
    @Override
    public BookCategory save(BookCategory bookCategory) {
        log.debug("Request to save book category: {}", bookCategory);
        return bookCategoryRepository.save(bookCategory);
    }

    @Override
    public Optional<BookCategory> partialUpdate(BookCategory bookCategory) {
        log.debug("Request to partially update book category : {}", bookCategory);

        BookCategory bk = bookCategoryRepository.findById(bookCategory.getId()).get();
        if (bk != null) {
            bk.setName(bookCategory.getName());
            return Optional.ofNullable(save(bk));
        }
        return null;
    }

    @Override
    public List<BookCategory> findAll() {
        List<BookCategory> categories = bookCategoryRepository.findAll();
        return categories.stream().collect(Collectors.toList());
    }

    @Override
    public Page<BookCategory> findAll(Pageable pageable) {
        log.debug("Request to get all book categories");
        return bookCategoryRepository.findAll(pageable);
    }

    @Override
    public Optional<BookCategory> findOne(Long id) {
        log.debug("Request to get book category : {}", id);
        return bookCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete book category : {}", id);
        bookCategoryRepository.deleteById(id);
    }
}
