package com.antchb.examples.hibernate.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="fingerprints", schema = "users_control")
public class Fingerprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fingerprint_id")
    private Long fingerprintId;

    @Column(name = "data")
    private Byte[] data;

    @Column(name = "notes")
    private String notes;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upd_dt")
    private Date updDate;

    // Bi-Directional Relationship
    // mappedBy refers to "fingerprint" field in User class
    @OneToOne(mappedBy = "fingerprint",
              cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    public Fingerprint() { }

    public Fingerprint(Scanner in) {
        System.out.println("\n### Enter values for a new fingerprint:\n");
        scanValues(in);
    }

    public void updateValues(Scanner scanner) {
        System.out.println("\n### Enter new values for the fingerprint:\n");
        scanValues(scanner);
    }
    
    private void scanValues(Scanner in) {
        data = null;

        System.out.print(String.format("%n[Notes] (%s): ", notes));
        notes = in.nextLine();
    }

    public void updateValues(Fingerprint fingerprint) {
        this.data = fingerprint.getData();
        this.notes = fingerprint.getNotes();
    }

    public Long getFingerprintId() {
        return fingerprintId;
    }

    public Byte[] getData() {
        return data;
    }

    public String getNotes() {
        return notes;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Fingerprint [data=" + Arrays.toString(data) + ", fingerprintId=" + fingerprintId + ", notes=" + notes
                + ", updDate=" + updDate + "]";
    }


}
