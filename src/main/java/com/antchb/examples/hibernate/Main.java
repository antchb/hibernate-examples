package com.antchb.examples.hibernate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.antchb.examples.hibernate.dao.IUserDAO;
import com.antchb.examples.hibernate.dao.UserDAO;
import com.antchb.examples.hibernate.entity.User;

public class Main {

    public static void main(String[] args) throws IOException {
        // SessionFactory. Reads the hibernate config file. It creates session objects. Heavy-weight object
        // Should be created once in an app

        // Session. Wraps a JDBC connection. Short-lived object
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
            Scanner in = new Scanner(System.in)) {
           
            System.out.println("Select a mode for testing:\n");
            System.out.println("\t[1]: CRUD Features (Created, Read, Update, Delete)");
            
            System.out.print("\n### Selected Option: ");

            String option = in.nextLine();

            switch(option) {
                case "1" -> crud(factory, in); 
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        }
    }
    
    private static void crud(SessionFactory factory, Scanner in) {
        IUserDAO userDao = new UserDAO(factory);

        displayAllUsers(userDao);
        addUser(userDao, in);
        displayAllUsers(userDao);
        updateUser(userDao, in);
        displayAllUsers(userDao);
        deleteUser(userDao, in);
        displayAllUsers(userDao);
    }

    private static void displayAllUsers(IUserDAO userDao) {
        List<User> users = userDao.getAll();

        System.out.println("\nAll Users:\n");

        if (users != null) {
            users.forEach(u -> System.out.println(u));
        }
    }

    private static void addUser(IUserDAO userDao, Scanner in) {
        User user = new User(in);
        userDao.add(user);
    }

    private static void updateUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to update a user record. Please, enter ID: ");

        Long id = in.nextLong();
        in.nextLine();
        Optional<User> user = userDao.get(id);

        if (!user.isPresent()) {
            System.out.println("Wrong ID. User not found");
            return;
        }

        user.get().updateValues(in);
        userDao.update(id, user.get());
    }

    private static void deleteUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to delete a user record. Please, enter ID: ");

        Long id = in.nextLong();
        userDao.delete(id);
    }
}
