package com.antchb.examples.hibernate.dao;

import java.util.List;
import java.util.Optional;

import com.antchb.examples.hibernate.entity.Course;

public interface ICourseDAO {

    Optional<Course> get(Long id, boolean includeUsers);
   
    void add(Course user);

    void delete(Long id);

    List<Course> getAll();

}
