package com.antchb.examples.hibernate.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.antchb.examples.hibernate.entity.Course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CourseDAO implements ICourseDAO {

    private SessionFactory factory;

    public CourseDAO(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Course> get(Long id, boolean includeUsers) {
        Optional<Course> course;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            course = Optional.ofNullable(session.get(Course.class, id));

            if (includeUsers && course.isPresent()) {
                Hibernate.initialize(course.get().getCourseUsers());
            }

            session.getTransaction().commit();
        }

        return course;
    }

    @Override
    public void add(Course course) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Course course = session.get(Course.class, id);

            if (course != null) {
                session.remove(course);
            }
            
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Course> getAll() {
        List<Course> courses;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Course> criteria = cb.createQuery(Course.class);
            Root<Course> root = criteria.from(Course.class);
            criteria.select(root);

            courses = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
        }

        return courses;
    }

}
