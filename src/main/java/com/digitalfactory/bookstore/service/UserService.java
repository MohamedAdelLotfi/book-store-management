package com.digitalfactory.bookstore.service;

import com.digitalfactory.bookstore.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * Save a book.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    User save(User user);

    /**
     * Partially updates a book.
     *
     * @param user the entity to update partially.
     * @return the persisted entity.
     */
    Optional<User> partialUpdate(User user);

    /**
     * Get all the users.
     *
     * @return the list of entities.
     */
    List<User> findAll();

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<User> findOne(Long id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * find User By User Name
     *
     * @param userName value
     * @return entity
     */
    User findByUserName(String userName);
}
