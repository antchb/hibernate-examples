package com.antchb.examples.hibernate.dao;

import com.antchb.examples.hibernate.entity.UserIdentity;

public interface IUserIdentityDAO {
   
    void add(Long userId, UserIdentity userIdentity);

    void delete(Long id);
}
