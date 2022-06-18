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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="user_identities", schema = "users_control")
public class UserIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identity_id")
    private Long identityId;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "type")
    private String type;

    @Column(name = "identifier")
    private String identifier;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upd_dt")
    private Date updDate;

    public UserIdentity() { }

    public UserIdentity(Scanner in) {
        System.out.println("\n### Enter values for a new user identity:\n");
        scanValues(in);
    }

    public void updateValues(Scanner in) {
        System.out.println("\n### Enter new values for the user:\n");
        scanValues(in);
    }
    
    private void scanValues(Scanner in) {
        System.out.print(String.format("%n[Type] (%s): ", type));
        type = in.nextLine();

        System.out.print(String.format("%n[Identifier] (%s): ", identifier));
        identifier = in.nextLine();
    }

    public Long getIdentityId() {
        return identityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserIdentity [identifier=" + identifier + ", identityId=" + identityId + ", type=" + type + ", updDate="
                + updDate + "]";
    }

}
