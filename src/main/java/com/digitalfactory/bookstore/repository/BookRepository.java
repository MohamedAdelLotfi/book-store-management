package com.digitalfactory.bookstore.repository;

import com.digitalfactory.bookstore.domain.Book;
import com.digitalfactory.bookstore.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query(nativeQuery = true, value = "select * From Book Where book_category_id=:book_category_Id")
    List<Book> findAllByBook_category_Id(Long book_category_Id);
}
