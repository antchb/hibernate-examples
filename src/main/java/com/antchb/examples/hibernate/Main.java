package com.antchb.examples.hibernate;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.antchb.examples.hibernate.dao.CourseDAO;
import com.antchb.examples.hibernate.dao.FingerprintDAO;
import com.antchb.examples.hibernate.dao.ICourseDAO;
import com.antchb.examples.hibernate.dao.IFingerprintDAO;
import com.antchb.examples.hibernate.dao.IUserDAO;
import com.antchb.examples.hibernate.dao.IUserIdentityDAO;
import com.antchb.examples.hibernate.dao.UserDAO;
import com.antchb.examples.hibernate.dao.UserIdentityDAO;
import com.antchb.examples.hibernate.entity.Course;
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
                .addAnnotatedClass(Course.class)
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
            System.out.println("\t[2]: Display all users filtered by first name");
            System.out.println("\t[3]: Add a new user");
            System.out.println("\t[4]: Delete a user");
            System.out.println("\t[5]: Update a user");
            System.out.println("\t[0]: EXIT");
            
            System.out.print("\n### Selected Option: ");

            option = in.nextLine();

            IUserDAO userDao = new UserDAO(factory);

            switch (option) {
                case "1" -> displayAllUsers(userDao);
                case "2" -> displayAllUsersLikeFirstName(userDao, in); 
                case "3" -> addUser(userDao, in);
                case "4" -> deleteUser(userDao, in); 
                case "5" -> updateUser(userDao, in);
                case "0" -> System.out.println("Bye!");
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        } while (!"0".equals(option));

    }
    
    private static void advancedMappings(SessionFactory factory, Scanner in) {
        String option;

        do {
            System.out.println("\n### Select an Advanced Mapping Action:\n");
            System.out.println("\t[1]: Display all users");
            System.out.println("\t[2]: Display fingerprint & user by fingerprint ID (Bi-Directional)");
            System.out.println("\t[3]: Display all courses");
            System.out.println("\t[4]: Display a user's courses (@ManyToMany)");
            System.out.println("\t[5]: Display a course's users (@ManyToMany)");
            System.out.println("\t[6]: Add a new user with fingerprint (@OneToOne)");
            System.out.println("\t[7]: Add a fingerprint (@OneToOne)");
            System.out.println("\t[8]: Add a user identity (@ManyToOne)");
            System.out.println("\t[9]: Add a course");
            System.out.println("\t[10]: Add a course to a user (@ManyToMany)");
            System.out.println("\t[11]: Delete a user (@OneToOne - Delete a fingerprint as well [CascadeType.ALL])");
            System.out.println("\t[12]: Delete a fingerprint (@OneToOne)");
            System.out.println("\t[13]: Delete a user identity (@ManyToOne)");
            System.out.println("\t[14]: Delete a course (@ManyToMany)");
            System.out.println("\t[15]: Delete a course from a user (@ManyToMany)");
            System.out.println("\t[0]: EXIT");
            
            System.out.print("\n### Selected Option: ");

            option = in.nextLine();

            IUserDAO userDao = new UserDAO(factory);
            IFingerprintDAO fingerprintDao = new FingerprintDAO(factory);
            IUserIdentityDAO userIdentityDao = new UserIdentityDAO(factory);
            ICourseDAO courseDao = new CourseDAO(factory);

            switch (option) {
                case "1" -> displayAllUsers(userDao);
                case "2" -> displayFingerprintWithRelatedUser(fingerprintDao, in);
                case "3" -> displayAllCourses(courseDao);
                case "4" -> displayUserCourses(userDao, in);
                case "5" -> displayCourseUsers(courseDao, in);
                case "6" -> addUserWithFingerprint(userDao, in);
                case "7" -> addFingerprint(fingerprintDao, in);
                case "8" -> addUserIdentity(userIdentityDao, in);
                case "9" -> addCourse(courseDao, in);
                case "10" -> addCourseToUser(courseDao, userDao, in);
                case "11" -> deleteUser(userDao, in);
                case "12" -> deleteFingerprint(fingerprintDao, in);
                case "13" -> deleteUserIdentity(userIdentityDao, in);
                case "14" -> deleteCourse(courseDao, in);
                case "15" -> deleteCourseFromUser(userDao, in);
                case "0" -> System.out.println("Bye!");
                default -> System.out.println("Wrong option was selected. Please, run again");
            }
        } while (!"0".equals(option));
    }

    // DISPLAY
    private static void displayAllUsers(IUserDAO userDao) {
        List<User> users = userDao.getAll();

        System.out.println("\n### All Users:\n");

        if (users != null) {
            users.forEach(System.out::println);
        }
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

    private static void displayAllCourses(ICourseDAO courseDao) {
        List<Course> courses = courseDao.getAll();

        System.out.println("\n### All Courses:\n");

        if (courses != null) {
            courses.forEach(System.out::println);
        }
    }

    private static void displayCourseUsers(ICourseDAO courseDao, Scanner in) {
        System.out.print("\n### Trying to display a course's users. Please, enter Course ID: ");

        Long courseId = Long.valueOf(in.nextLine());

        Optional<Course> course = courseDao.get(courseId, true);

        if (!course.isPresent()) {
            System.out.println("\nWrong Course ID. Course not found\n");
            return;
        }

        List<User> courseUsers = course.get().getCourseUsers();
        
        if (courseUsers != null) {
            courseUsers.forEach(System.out::println);
        }
    }

    private static void displayUserCourses(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to display a user's courses. Please, enter User ID: ");

        Long userId = Long.valueOf(in.nextLine());

        Optional<User> user = userDao.get(userId, true);

        if (!user.isPresent()) {
            System.out.println("\nWrong User ID. User not found\n");
            return;
        }
        
        List<Course> userCourses = user.get().getCourses();

        if (userCourses != null) {
            userCourses.forEach(System.out::println);
        }
    }

    // ADD
    private static void addUser(IUserDAO userDao, Scanner in) {
        User user = new User(in, null);
        userDao.add(user);
    }

    private static void addFingerprint(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to add a fingerprint to a user record. Please, enter a User ID: ");

        Long userId = Long.parseLong(in.nextLine());

        Fingerprint fingerprint = new Fingerprint(in);
        fingerprintDao.add(userId, fingerprint);
    }
    
    private static void addUserWithFingerprint(IUserDAO userDao, Scanner in) {
        Fingerprint fingerprint = new Fingerprint(in);
        User user = new User(in, fingerprint);
        userDao.add(user);
    }

    private static void addUserIdentity(IUserIdentityDAO userIdentityDao, Scanner in) {
        System.out.print("\n### Trying to add a user identity to a user. Please, enter User ID: ");
        Long userId = Long.parseLong(in.nextLine());

        UserIdentity userIdentity = new UserIdentity(in);
        userIdentityDao.add(userId, userIdentity);
    }

    private static void addCourse(ICourseDAO courseDao, Scanner in) {
        Course course = new Course(in);
        courseDao.add(course);
    }

    private static void addCourseToUser(ICourseDAO courseDao, IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to add a course to the user...\n");

        System.out.print("Course ID: ");
        Long courseId = Long.valueOf(in.nextLine());
        Optional<Course> course = courseDao.get(courseId, false);

        if (!course.isPresent()) {
            System.out.println("Wrong Course ID. Course not found. ID: " + courseId);
            return;
        }

        System.out.print("User ID: ");
        Long userId = Long.valueOf(in.nextLine());

        userDao.addCourse(userId, course.get());
    }

    // DELETE
    private static void deleteUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to delete a user record. Please, enter a User ID: ");

        Long id = Long.parseLong(in.nextLine());
        userDao.delete(id);
    }

    private static void deleteFingerprint(IFingerprintDAO fingerprintDao, Scanner in) {
        System.out.print("\n### Trying to delete a fingerprint record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        fingerprintDao.delete(id);
    }

    private static void deleteUserIdentity(IUserIdentityDAO userIdentityDao, Scanner in) {
        System.out.print("\n### Trying to delete a user identity record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        userIdentityDao.delete(id);
    }

    private static void deleteCourseFromUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to delete a course from the user...\n");

        System.out.print("User ID: ");
        Long userId = Long.valueOf(in.nextLine());

        System.out.print("Course ID: ");
        Long courseId = Long.valueOf(in.nextLine());

        userDao.deleteCourse(userId, courseId);
    }

    private static void deleteCourse(ICourseDAO courseDao, Scanner in) {
        System.out.print("\n### Trying to delete a course. Please, enter a Course ID: ");

        Long id = Long.parseLong(in.nextLine());
        courseDao.delete(id);
    }

    // UPDATE
    private static void updateUser(IUserDAO userDao, Scanner in) {
        System.out.print("\n### Trying to update a user record. Please, enter ID: ");

        Long id = Long.parseLong(in.nextLine());
        Optional<User> user = userDao.get(id, false);

        if (!user.isPresent()) {
            System.out.println("\nWrong ID. User not found\n");
            return;
        }

        user.get().updateValues(in);
        userDao.update(id, user.get());
    }

}
