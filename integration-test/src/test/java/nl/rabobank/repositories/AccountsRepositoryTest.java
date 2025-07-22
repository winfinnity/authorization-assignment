package nl.rabobank.repositories;


import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.mongo.entities.AccountDto;

import nl.rabobank.mongo.repositories.AccountsRepository;
import nl.rabobank.mongo.repositories.PowerOfAttorneysRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootTest(classes = RaboAssignmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountsRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    PowerOfAttorneysRepository powerOfAttorneysRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        accountsRepository.deleteAll();
        powerOfAttorneysRepository.deleteAll();
    }

    @Test
    void testCreateDuplicateAccountNumberShouldFail(){
        AccountDto account = AccountDto.builder().accountNumber("piet").build();
        accountsRepository.insert(account);
        Assertions.assertThrows(DuplicateKeyException.class,()->accountsRepository.insert(account));
    }

}