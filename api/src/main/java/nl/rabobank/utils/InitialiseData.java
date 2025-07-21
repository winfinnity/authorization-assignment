package nl.rabobank.utils;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;

import nl.rabobank.mongo.entities.AccountDto;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class InitialiseData {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

         var accounts = objectMapper.readValue(
            new ClassPathResource("/data/accounts.json").getFile(), new TypeReference<List<AccountDto>>() {
            });

        mongoTemplate.insertAll(accounts);
        log.info("Accounts pre-initialised: {}", accounts);
        log.info("Data initialization completed successfully");

    }
}
