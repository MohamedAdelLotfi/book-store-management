package com.digitalfactory.bookstore.web;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.exceptions.BadRequestAlertException;
import com.digitalfactory.bookstore.repository.BookRepository;
import com.digitalfactory.bookstore.service.BookService;
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
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);

    private static final String ENTITY_NAME = "book";
    private static final String applicationName = "BookStore";

    private final BookService bookService;

    private final BookRepository bookRepository;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @PostMapping(
            value = "/book",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public EntityId<Long> createBook(@RequestBody Book book)
            throws URISyntaxException {
        log.debug("REST request to save book : {}", book);
        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Book result = bookService.save(book);

        EntityId<Long> entity = new EntityId<>();
        entity.setId(result.getId());
        return entity;
    }

    @PutMapping(
            value = "/book/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Book> updateBook(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody Book book
    ) throws URISyntaxException {
        log.debug("REST request to update Book : {}, {}", id, book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Book result = bookService.save(book);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(
            value = "/book/{id}",
            consumes = {"application/json", "application/merge-patch+json"},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Book> partialUpdateBook(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Book book
    ) throws URISyntaxException {
        log.debug("REST request to update Book : {}, {}", id, book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Book result = bookService.save(book);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping(
            value = "/book/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<Book> book = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(book);
    }

    @GetMapping(
            value = "/book-pageable",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Page<Book>> getBookPageable(Pageable pageable) {
        log.debug("REST request to get Book : {}", pageable);
        Page<Book> books = bookService.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping(
            value = "/books",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Book>> getBookAll() {
        log.debug("REST request to get Book : {}");
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete book : {}", id);
        bookService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(
            value = "/available-books",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Book>> getAvailableBooks() {
        log.debug("REST request to get Book : {}");
        List<Book> books = bookService.findAvailableBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping(
            value = "/available-books-category/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Book>> getAvailableBooksByCategory(@PathVariable Long id) {
        log.debug("REST request to get Book : {}");
        List<Book> books = bookService.findAvailableBooksByCategory(id);
        return ResponseEntity.ok(books);
    }
}
