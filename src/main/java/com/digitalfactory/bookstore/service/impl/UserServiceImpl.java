package com.digitalfactory.bookstore.service.impl;

import com.digitalfactory.bookstore.domain.User;
import com.digitalfactory.bookstore.repository.UserRepository;
import com.digitalfactory.bookstore.service.UserService;
import com.digitalfactory.bookstore.service.enums.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User save(User user) {
        log.debug("Request to save user : {}", user);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> partialUpdate(User user) {
        log.debug("Request to partially update user : {}", user);

        User bk = userRepository.findById(user.getId()).get();
        if (bk != null) {
            bk.setAddress(user.getAddress());
            bk.setPhone(user.getPhone());
            //bk.setType(user.getType());
            bk.setCivilId(user.getCivilId());
            bk.setEmail(user.getEmail());
            return Optional.ofNullable(save(bk));
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        List<User> usrs = new ArrayList<>();
        for (User user : users) {
            String userType = user.getType();
            if (!StringUtils.isEmpty(userType)) {
                String user_type = UserType.getUserType(userType).name();
                if (user_type.length() == 2 && user_type != null) {
                    user.setType(user_type);
                }
            }
            usrs.add(user);
        }
        return usrs;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all users");
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findOne(Long id) {
        log.debug("Request to get user : {}", id);
        Optional<User> user =  userRepository.findById(id);
        String userType = user.get().getType();
        if (!StringUtils.isEmpty(userType)) {
            String user_type = UserType.getUserType(userType).name();
            if (user_type.length() == 2 && user_type != null) {
                user.get().setType(user_type);
            }
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete user : {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public User findByUserName(String userName) {
        log.debug("Request to return user by username ");
        return userRepository.findByUsername(userName).get();
    }
}
