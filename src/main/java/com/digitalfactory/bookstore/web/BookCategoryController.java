package com.digitalfactory.bookstore.web;

import com.digitalfactory.bookstore.domain.BookCategory;
import com.digitalfactory.bookstore.exceptions.BadRequestAlertException;
import com.digitalfactory.bookstore.repository.BookCategoryRepository;
import com.digitalfactory.bookstore.service.BookCategoryService;
import com.digitalfactory.bookstore.web.response.EntityId;
import com.digitalfactory.bookstore.web.response.HeaderUtil;
import com.digitalfactory.bookstore.web.response.ResponseUtil;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class BookCategoryController {

    private final Logger log = LoggerFactory.getLogger(BookCategoryController.class);

    private static final String ENTITY_NAME = "book-category";
    private static final String applicationName = "BookStore";

    private final BookCategoryService bookCategoryService;

    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryController(BookCategoryService bookCategoryService, BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryService = bookCategoryService;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @PostMapping(
            value = "/book-category",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public EntityId<Long> createBookCategory(@RequestBody BookCategory bookCategory)
            throws URISyntaxException {
        log.debug("REST request to save book category : {}", bookCategory);
        if (bookCategory.getId() != null) {
            throw new BadRequestAlertException("A new book category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookCategory result = bookCategoryService.save(bookCategory);

        EntityId<Long> entity = new EntityId<>();
        entity.setId(result.getId());
        return entity;
    }

    @PutMapping(
            value = "/book-category/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<BookCategory> updateBookCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody BookCategory bookCategory
    ) throws URISyntaxException {
        log.debug("REST request to update Book Category : {}, {}", id, bookCategory);
        if (bookCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookCategory result = bookCategoryService.save(bookCategory);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(
            value = "/book-category/{id}",
            consumes = { "application/json", "application/merge-patch+json" },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<BookCategory> partialUpdateBookCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody BookCategory bookCategory
    ) throws URISyntaxException {
        log.debug("REST request to update Book Category : {}, {}", id, bookCategory);
        if (bookCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookCategory result = bookCategoryService.save(bookCategory);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping(
            value = "/book-category/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<BookCategory> getBookCategory(@PathVariable Long id) {
        log.debug("REST request to get Book Category : {}", id);
        Optional<BookCategory> bookCategory = bookCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookCategory);
    }

    @GetMapping(
            value = "/book-category-pageable",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Page<BookCategory>> getBookCategoryPageable(Pageable pageable) {
        log.debug("REST request to get Book Category : {}", pageable);
        Page<BookCategory> bookCategories = bookCategoryService.findAll(pageable);
        return ResponseEntity.ok(bookCategories);
    }

    @GetMapping(
            value = "/book-categories",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<BookCategory>> getBookCategoryAll() {
        log.debug("REST request to get Book Category : {}");
        List<BookCategory> bookCategories = bookCategoryService.findAll();
        return ResponseEntity.ok(bookCategories);
    }

    @DeleteMapping("/book-category/{id}")
    public ResponseEntity<Void> deleteBookCategory(@PathVariable Long id) {
        log.debug("REST request to delete book category : {}", id);
        bookCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
