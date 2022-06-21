package com.antchb.examples.hibernate.dao;

import java.util.List;
import java.util.Optional;

import com.antchb.examples.hibernate.entity.Course;
import com.antchb.examples.hibernate.entity.User;

public interface IUserDAO {
   
    Optional<User> get(Long id, boolean includeCourses);
    
    void add(User user);

    void delete(Long id);

    void update(Long id, User user);

    List<User> getAll();

    List<User> getAllByFirstName(String firstName);

    void addCourse(Long id, Course course);

    void deleteCourse(Long id, Long courseId);

}
