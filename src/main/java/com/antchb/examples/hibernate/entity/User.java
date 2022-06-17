package com.antchb.examples.hibernate.entity;

import java.util.Date;
import java.util.Scanner;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upd_dt")
    private Date updDate;

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
        this.fingerprint = user.getFingerprint();
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

    @Override
    public String toString() {
        return "User [userId=" + userId +", firstName=" + firstName + ", lastName=" + lastName +
               ", fingerprint=" + fingerprint + ", updDate=" + updDate + "]";
    }

}
