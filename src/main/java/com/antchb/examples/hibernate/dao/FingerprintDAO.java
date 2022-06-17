package com.antchb.examples.hibernate.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.antchb.examples.hibernate.entity.Fingerprint;

public class FingerprintDAO implements IFingerprintDAO {

    private SessionFactory factory;

    public FingerprintDAO(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Fingerprint> get(Long id) {
        Optional<Fingerprint> fingerprint;

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            fingerprint = Optional.ofNullable(session.get(Fingerprint.class, id));
            session.getTransaction().commit();
        }

        return fingerprint;
    }

    @Override
    public void delete(Long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            Fingerprint fingerprint = session.get(Fingerprint.class, id);

            if (fingerprint != null) {
                fingerprint.getUser().setFingerprint(null);
                session.remove(fingerprint);
            }
            
            session.getTransaction().commit();
        }
    }

}
