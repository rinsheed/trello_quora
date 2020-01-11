package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthEntity getUserAuthFromAuthToken(String authToken){

        return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).getSingleResult();
    }

}