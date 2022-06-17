package com.antchb.examples.hibernate.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.antchb.examples.hibernate.entity.User;
import com.antchb.examples.hibernate.entity.User_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserDAO implements IUserDAO {

    private SessionFactory factory;

    public UserDAO(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<User> get(Long id) {
        Optional<User> user;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            user = Optional.ofNullable(session.get(User.class, id));
            session.getTransaction().commit();
        }

        return user;
    }

    @Override
    public void add(User user) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        User user;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }
            
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Long id, User user) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            User dbUser = session.get(User.class, id);

            if (dbUser != null) {
                dbUser.updateValues(user);
            }
            
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<User> criteria = cb.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root);

            users = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
        }

        return users;
    }

    @Override
    public List<User> getAllByFirstName(String firstName) {
        List<User> users;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<User> criteria = cb.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root);
            criteria.where(cb.like(root.get(User_.firstName), firstName));

            users = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
        }

        return users;
    }

}
