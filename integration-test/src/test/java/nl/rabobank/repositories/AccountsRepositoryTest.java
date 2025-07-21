package nl.rabobank.repositories;

import java.util.List;

import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.mongo.entities.AccountDto;
import nl.rabobank.mongo.entities.AccountType;
import nl.rabobank.mongo.entities.PowerOfAttorneyDto;
import nl.rabobank.mongo.repositories.AccountsRepository;
import nl.rabobank.mongo.repositories.PowerOfAttorneysRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Test
    void testCreateDuplicateAuthorizationShouldFail(){
        AccountDto account = AccountDto.builder().accountNumber("piet").build();

        account = accountsRepository.insert(account);


        PowerOfAttorneyDto power1 = PowerOfAttorneyDto.builder()
            .grantor("piet")
            .grantee("miep")
            .account(account)
            .authorization(Authorization.WRITE.toString())
            .build();
        PowerOfAttorneyDto power2 = PowerOfAttorneyDto.builder()
            .grantor("piet")
            .grantee("miep")
            .account(account)
            .authorization(Authorization.READ.toString())
            .build();

        powerOfAttorneysRepository.insert(power1);
        powerOfAttorneysRepository.insert(power2);

        var result = powerOfAttorneysRepository.findByGrantee("miep");
        System.out.println(result);
        accountsRepository.deleteAll();
        result = powerOfAttorneysRepository.findByGrantee("miep");
        System.out.println(result);
    }

    @Test
    void testFindAuthorization(){
        AccountDto account = AccountDto.builder().accountNumber("piet").build();
        AccountDto account2 = AccountDto.builder().accountNumber("miep").build();

        accountsRepository.insert(account);
        accountsRepository.insert(account2);

        account = accountsRepository.findByAccountNumber("hein");
        System.out.println(account);
        account = accountsRepository.findByAccountNumber("miep");
        System.out.println(account);
        account.setBalance(0.0);
        accountsRepository.save(account);

    }

    @Test
    void testMongoTemplate() throws JsonProcessingException {
        AccountDto account = AccountDto.builder().accountNumber("mongotemplate").accountType(AccountType.PAYMENT).build();

        account = mongoTemplate.insert(account);
        var accounts = mongoTemplate.findAll(AccountDto.class);
        System.out.println(accounts);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "[{ \"accountNumber\":\"00003\", \"accountType\":\"PAYMENT\"}]";
        var listOfAccounts = objectMapper.readValue(jsonString, new TypeReference<List<AccountDto>>(){});
        System.out.println(listOfAccounts);
        mongoTemplate.insertAll(listOfAccounts);
    }

}