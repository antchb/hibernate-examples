package com.antchb.examples.hibernate.dao;

import java.util.List;
import java.util.Optional;

import com.antchb.examples.hibernate.entity.User;

public interface IUserDAO {
   
    Optional<User> get(Long id);
    
    void add(User user);

    void delete(Long id);

    void update(Long id, User user);

    List<User> getAll();

    List<User> getAllByFirstName(String firstName);

}
