package com.digitalfactory.bookstore.service;

import com.digitalfactory.bookstore.domain.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookCategoryService {
    /**
     * Save a book.
     *
     * @param bookCategory the entity to save.
     * @return the persisted entity.
     */
    BookCategory save(BookCategory bookCategory);

    /**
     * Partially updates a book.
     *
     * @param bookCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookCategory> partialUpdate(BookCategory bookCategory);

    /**
     * Get all the book Categories.
     *
     * @return the list of entities.
     */
    List<BookCategory> findAll();

    /**
     * Get all the book Categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookCategory> findAll(Pageable pageable);

    /**
     * Get the "id" book Category.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookCategory> findOne(Long id);

    /**
     * Delete the "id" book category.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
