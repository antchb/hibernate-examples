package com.antchb.examples.hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.antchb.examples.hibernate.dao.FingerprintDAO;
import com.antchb.examples.hibernate.dao.IFingerprintDAO;
import com.antchb.examples.hibernate.dao.IUserDAO;
import com.antchb.examples.hibernate.dao.IUserIdentityDAO;
import com.antchb.examples.hibernate.dao.UserDAO;
import com.antchb.examples.hibernate.dao.UserIdentityDAO;
import com.antchb.examples.hibernate.entity.Fingerprint;
import com.antchb.examples.hibernate.entity.User;
import com.antchb.examples.hibernate.entity.UserIdentity;

public class Main {

    public static void main(String[] args) {
        // SessionFactory. Reads the hibernate config file. It creates session objects. Heavy-weight object
        // Should be created once in an app

        // Session. Wraps a JDBC connection. Short-lived object
        try (SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Fingerprint.class)
                .addAnnotatedClass(UserIdentity.class)
                .buildSessionFactory();
            Scanner in = new Scanner(System.in)) {
           
            System.out.println("Select a mode for testing:\n");
            System.out.println("\t[1]: CRUD Features (Created, Read, Update, Delete)");
            System.out.println("\t[2]: Hibernate Advanced Mappings");
            
            System.out.print("\n### Selected Option: ");

            String option = in.nextLine();

            switch (option) {
                case "1" -> crud(factory, in);
                case "2" -> advancedMappings(factory, in);
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        }
    }

    private static void crud(SessionFactory factory, Scanner in) {
        String option;

        do {
            System.out.println("\n### Select a CRUD action:\n");
            System.out.println("\t[1]: Display all users");
            System.out.println("\t[2]: Add a new user");
            System.out.println("\t[3]: Update a user");
            System.out.println("\t[4]: Delete a user");
            System.out.println("\t[5]: Display all users filtered by first name");
            System.out.println("\t[0]: EXIT");
            
            System.out.print("\n### Selected Option: ");

            option = in.nextLine();

            IUserDAO userDao = new UserDAO(factory);

            switch (option) {
                case "1" -> displayAllUsers(userDao);
                case "2" -> addUser(userDao, in);
                case "3" -> updateUser(userDao, in);
                case "4" -> deleteUser(userDao, in); 
                case "5" -> displayAllUsersLikeFirstName(userDao, in); 
                case "0" -> System.out.println("Bye!");
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        } while (!"0".equals(option));

    }
    
    private static void advancedMappings(SessionFactory factory, Scanner in) {
        String option;

        do {
            System.out.println("\n### Select an Advanced Mapping Action:\n");
            System.out.println("\t[1]: Display All Users");
            System.out.println("\t[2]: Display Fingerprint & User by Fingerprint ID (Bi-Directional)");
            System.out.println("\t[3]: Add a new User with Fingerprint (@OneToOne)");
            System.out.println("\t[4]: Delete a User (@OneToOne - Deletes with the Fingerprint [CascadeType.ALL])");
            System.out.println("\t[5]: Add a Fingerprint (@OneToOne)");
            System.out.println("\t[6]: Delete a Fingerprint (@OneToOne)");
            System.out.println("\t[7]: Add a user identity (@ManyToOne)");
            System.out.println("\t[8]: Delete a user identity (@ManyToOne)");
            System.out.println("\t[0]: EXIT");
            
            System.out.print("\n### Selected Option: ");

            option = in.nextLine();

            IUserDAO userDao = new UserDAO(factory);
            IFingerprintDAO fingerprintDao = new FingerprintDAO(factory);
            IUserIdentityDAO userIdentityDao = new UserIdentityDAO(factory);

            switch (option) {
                case "1" -> displayAllUsers(userDao);
                case "2" -> displayFingerprintWithRelatedUser(fingerprintDao, in);
                case "3" -> addUserWithFingerprint(userDao, in);
                case "4" -> deleteUser(userDao, in);
                case "5" -> addFingerprint(fingerprintDao, in);
                case "6" -> deleteFingerprint(fingerprintDao, in);
                case "7" -> addUserIdentity(userIdentityDao, in);
                case "8" -> deleteUserIdentity(userIdentityDao, in);
                case "0" -> System.out.println("Bye!");
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        } while (!"0".equals(option));
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
        System.out.print("\n### Trying to delete a user record. Please, enter a User ID: ");

        Long id = Long.parseLong(in.nextLine());
        userDao.delete(id);
    }

    private static void displayAllUsersLikeFirstName(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to display users with the first name like entered value.\n" +
                         "\nPlease, enter a user's First Name: ");

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

    private static void addUserWithFingerprint(IUserDAO userDao, Scanner in) {
        Fingerprint fingerprint = new Fingerprint(in);
        User user = new User(in, fingerprint);
        userDao.add(user);
    }

    private static void addFingerprint(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to add a fingerprint to a user record. Please, enter a User ID: ");

        Long userId = Long.parseLong(in.nextLine());

        Fingerprint fingerprint = new Fingerprint(in);
        fingerprintDao.add(userId, fingerprint);
    }

    private static void deleteFingerprint(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to delete a fingerprint record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        fingerprintDao.delete(id);
    }

    private static void addUserIdentity(IUserIdentityDAO userIdentityDao, Scanner in) {
        System.out.print("\n### Trying to add a user identity to a user. Please, enter User ID: ");
        Long userId = Long.parseLong(in.nextLine());

        UserIdentity userIdentity = new UserIdentity(in);
        userIdentityDao.add(userId, userIdentity);
    }

    private static void deleteUserIdentity(IUserIdentityDAO userIdentityDao, Scanner in) {
        System.out.print("\n### Trying to delete a user identity record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        userIdentityDao.delete(id);
    }
}
