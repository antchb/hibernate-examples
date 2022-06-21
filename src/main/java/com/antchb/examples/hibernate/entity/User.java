package com.antchb.examples.hibernate.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="users", schema = "users_control")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fingerprint_id")
    private Fingerprint fingerprint;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    List<UserIdentity> userIdentities;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upd_dt")
    private Date updDate;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_courses", schema = "users_control", joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "course_id")) 
    private List<Course> courses;

    public User() { }

    public User(Scanner in, Fingerprint fingerprint) {
        System.out.println("\n### Enter values for a new user:\n");
        scanValues(in);
        this.fingerprint = fingerprint;
    }

    public void updateValues(Scanner in) {
        System.out.println("\n### Enter new values for the user:\n");
        scanValues(in);
    }
    
    private void scanValues(Scanner in) {
        System.out.print(String.format("%n[First Name] (%s): ", firstName));
        firstName = in.nextLine();

        System.out.print(String.format("%n[Last Name] (%s): ", lastName));
        lastName = in.nextLine();
    }

    public void updateValues(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void addUserIdentity(UserIdentity userIdentity) {
        if (userIdentities == null) {
            userIdentities = new ArrayList<>();
        }
        userIdentity.setUser(this);

        userIdentities.add(userIdentity);
    }

    public List<UserIdentity> getUserIdentities() {
        return userIdentities;
    }

    public void deleteUserIdentity(Long identityId) {
        if (userIdentities == null) {
            return;
        }

        userIdentities.removeIf(i -> identityId.equals(i.getIdentityId()));
    }

    public void addCourse(Course course) {
        if (courses == null) {
            courses = new ArrayList<>();
        }

        courses.add(course);
    }

    public void deleteCourse(Long courseId) {
        if (courses == null) {
            return;
        }

        courses.removeIf(i -> courseId.equals(i.getCourseId()));
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", updDate="
                + updDate + ", " + fingerprint + ", " +  userIdentities + "]";
    }

}
