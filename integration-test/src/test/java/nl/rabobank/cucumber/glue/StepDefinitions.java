package nl.rabobank.cucumber.glue;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;

import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.model.CreateAuthorizationRequest;
import nl.rabobank.mongo.entities.AccountDto;
import nl.rabobank.mongo.entities.AccountType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(classes = RaboAssignmentApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepDefinitions {

    @Autowired
    private  MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    private String uri;


    private Response response;

    private AccountDto account;


    @PostConstruct
    public void init() throws IOException {

        uri = "http://localhost:" + port;
        setUpAccounts();
    }

    @When("I send a create authorization request with body {string} {string} {string} {string}")
    public void post_create_authorization_request_with_body(
        String grantor, String grantee, String accountNumber, String authorization) throws JsonProcessingException {
        var body = new CreateAuthorizationRequest(grantor,grantee,accountNumber, Authorization.valueOf(authorization));
        var jsonBody = objectMapper.writeValueAsString(body);
        sendRequest("POST","/api/authorizations",jsonBody);
        log.info("Authorization request has been sent");
    }

    @And("I send request to get authorizations for {string}")
    public void get_authorizations_for_grantee(String grantee) {
        sendRequest("GET","/api/authorizations?grantee="+grantee,null);
        log.info("Authorizations for grantee {}", grantee);
    }

    @Then("I should get {int} status code")
    public void the_response_status_code_should_be(int statusCode) {
        log.info("Status code is {}", response.getStatusCode());
        response.then().statusCode(statusCode);
    }

    @Then("I should get {int} authorizations")
    public void authorizations_count_should_be(int count) {
        List<Object> authorizations = response.getBody().as(new TypeRef<List<Object>>(){});
        if (authorizations.size() != count) {
            throw new AssertionError("Expected " + count + " authorizations, but got " + authorizations.size());
        }
        log.info("Authorizations count is {}", authorizations.size());
    }

    @Given("Create a new account {string}")
    public void create_new_account(String accountNumber)  {
        var newAccount = AccountDto.builder()
            .accountType(AccountType.PAYMENT)
            .accountNumber(accountNumber)
            .accountHolderName("new account")
            .balance(0.0)
            .build();
        account = mongoTemplate.insert(newAccount);
    }

    @But("Account got deleted")
    public void account_deleted() {
        mongoTemplate.remove(account);
    }



    private void sendRequest(String method, String endpoint, String body) {
        switch (method.toUpperCase()) {
        case "GET":
            response = given().when().get(uri + endpoint);
            break;
        case "POST":
            response = given().when()
                .contentType(ContentType.JSON).body(body).post(uri + endpoint);
            break;
        default:
            throw new IllegalArgumentException("Invalid method: " + method);
        }
    }

    private void setUpAccounts() throws IOException {

        var accounts = objectMapper.readValue(
            new ClassPathResource("/data/accounts.json").getFile(), new TypeReference<List<AccountDto>>() {
            });
        try {
            mongoTemplate.insertAll(accounts);
            log.info("Accounts setup for test: {}", accounts);
        }
        catch (Exception e) {
            log.info("Accounts already setup");
        }
    }


}
