package com.antchb.examples.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.antchb.examples.hibernate.entity.User;
import com.antchb.examples.hibernate.entity.UserIdentity;

public class UserIdentityDAO implements IUserIdentityDAO {

    private SessionFactory factory;

    public UserIdentityDAO(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void add(Long userId, UserIdentity userIdentity) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);

            if (user == null) {
                System.out.println("Error! User not found! ID: " + userId);
                return;
            }

            userIdentity.setUser(user);
            user.addUserIdentity(userIdentity);
            session.persist(user);
            
            session.getTransaction().commit();
        }
    }
  
    @Override
    public void delete(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            UserIdentity userIdentity = session.get(UserIdentity.class, id);

            if (userIdentity != null) {
                userIdentity.getUser().deleteUserIdentity(userIdentity.getIdentityId());
                session.remove(userIdentity);
            }
            
            session.getTransaction().commit();
        }
    }
}
