package com.antchb.examples.hibernate.dao;

import java.util.Optional;

import com.antchb.examples.hibernate.entity.Fingerprint;

public interface IFingerprintDAO {
   
    Optional<Fingerprint> get(Long id);

    void delete(Long id);

    void add(Long userId, Fingerprint fingerprint);

}
