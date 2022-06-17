package com.antchb.examples.hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.antchb.examples.hibernate.dao.FingerprintDAO;
import com.antchb.examples.hibernate.dao.IFingerprintDAO;
import com.antchb.examples.hibernate.dao.IUserDAO;
import com.antchb.examples.hibernate.dao.UserDAO;
import com.antchb.examples.hibernate.entity.Fingerprint;
import com.antchb.examples.hibernate.entity.User;

public class Main {

    public static void main(String[] args) {
        // SessionFactory. Reads the hibernate config file. It creates session objects. Heavy-weight object
        // Should be created once in an app

        // Session. Wraps a JDBC connection. Short-lived object
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Fingerprint.class)
                .buildSessionFactory();
            Scanner in = new Scanner(System.in)) {
           
            System.out.println("Select a mode for testing:\n");
            System.out.println("\t[1]: CRUD Features (Created, Read, Update, Delete)");
            System.out.println("\t[2]: Hibernate Advanced Mappings");
            
            System.out.print("\n### Selected Option: ");

            String option = in.nextLine();

            switch(option) {
                case "1" -> crud(factory, in);
                case "2" -> advancedMappings(factory, in);
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
        displayAllUsersLikeFirstName(userDao, in);
    }
    
    private static void advancedMappings(SessionFactory factory, Scanner in) {
        // @OneToOne
        IUserDAO userDao = new UserDAO(factory);

        displayAllUsers(userDao);
        addUserWithFingerprint(userDao, in);
        displayAllUsers(userDao);
        deleteUser(userDao, in);

        // Bi-Directional Relation 
        IFingerprintDAO fingerprintDao = new FingerprintDAO(factory);
        displayFingerprintWithRelatedUser(fingerprintDao, in);
        deleteFingerprint(fingerprintDao, in);
        displayAllUsers(userDao);
    }

    private static void displayAllUsers(IUserDAO userDao) {
        List<User> users = userDao.getAll();

        System.out.println("\n### All Users:\n");

        if (users != null) {
            users.forEach(System.out::println);
        }
    }

    private static void addUser(IUserDAO userDao, Scanner in) {
        User user = new User(in, null);
        userDao.add(user);
    }
    
    private static void addUserWithFingerprint(IUserDAO userDao, Scanner in) {
        Fingerprint fingerprint = new Fingerprint(in);
        User user = new User(in, fingerprint);
        userDao.add(user);
    }

    private static void updateUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to update a user record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        Optional<User> user = userDao.get(id);

        if (!user.isPresent()) {
            System.out.println("\nWrong ID. User not found\n");
            return;
        }

        user.get().updateValues(in);
        userDao.update(id, user.get());
    }

    private static void deleteUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to delete a user record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        userDao.delete(id);
    }

    private static void displayAllUsersLikeFirstName(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to display users with the first name like entered value.\n" +
                         "\nPlease, enter [First Name]: ");

        String firstName = in.nextLine();
        List<User> users = userDao.getAllByFirstName(firstName);

        if (users != null) {
            users.forEach(System.out::println);
        }
    }

    private static void displayFingerprintWithRelatedUser(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to get fingerprint and related user record. Please, enter Fingerprint ID: ");

        Long id = Long.parseLong(in.nextLine());
        Optional<Fingerprint> fingerprint = fingerprintDao.get(id);

        if (!fingerprint.isPresent()) {
            System.out.println("\nWrong ID. Fingerprint not found\n");
            return;
        }

        System.out.println();
        System.out.println(fingerprint.get());
        System.out.println(fingerprint.get().getUser());
    }

    private static void deleteFingerprint(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to delete a fingerprint record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        fingerprintDao.delete(id);
    }
}
