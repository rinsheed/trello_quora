package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;

@Repository
public class QuestionDao {
    @PersistenceContext
    private EntityManager entityManager;


    public QuestionEntity createQuestion(QuestionEntity questionEntity) throws AuthorizationFailedException {
        try {
            entityManager.persist(questionEntity);
            return questionEntity;
        } catch (PersistenceException ex) {
            Throwable t = ex.getCause();

            if (t instanceof ConstraintViolationException) {

                if (((ConstraintViolationException) t).getConstraintName().toString()
                        .equalsIgnoreCase("question_accessToken_key")) {
                    throw new AuthorizationFailedException("ATHR-001",
                            "User has not signed in");
                } else if (((ConstraintViolationException) t).getConstraintName().toString()
                        .equalsIgnoreCase("users_uuid_key")) {
                    throw new AuthorizationFailedException("ATHR-002",
                            "User is signed out.Sign in first to post a question");
                }
            }
            return null;
        }
    }


    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("findAllQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity findQuestionById(String questionId) {
        try {
            return entityManager.createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity updateQuestionById(QuestionEntity question,String content) {
        QuestionEntity editedQuestion = entityManager.find(QuestionEntity.class, question.getId());
        editedQuestion.setContent(content);
        return entityManager.merge(editedQuestion);
    }

    public void deleteQuestionById(QuestionEntity questionEntity) {
        QuestionEntity deleteQuestion = entityManager.find(QuestionEntity.class, questionEntity.getId());
        entityManager.remove(deleteQuestion);
    }

    public List<QuestionEntity> getAllQuestionsByUserId(UserEntity userEntity) {
        try {
            return entityManager.createNamedQuery("getQuestionsByUserId", QuestionEntity.class)
                    .setParameter("user", userEntity).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }


}
