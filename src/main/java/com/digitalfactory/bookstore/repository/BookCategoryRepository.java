package com.digitalfactory.bookstore.repository;

import com.digitalfactory.bookstore.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long>, JpaSpecificationExecutor<BookCategory> {
}
