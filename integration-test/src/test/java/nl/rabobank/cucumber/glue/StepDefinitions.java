package nl.rabobank.cucumber.glue;

import static io.restassured.RestAssured.given;

import jakarta.annotation.PostConstruct;

import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.model.CreateAuthorizationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = RaboAssignmentApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class StepDefinitions {

    @Autowired
    private  MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    private String uri;

    private Response response;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @When("I send a {string} request to {string} with body {string} {string} {string} {string}")
    public void iSendAMethodRequestToEndpointWithBody(String method, String endpoint,
        String grantor, String grantee, String accountNumber, String authorization) throws JsonProcessingException {
        var body = new CreateAuthorizationRequest(grantor,grantee,accountNumber, Authorization.valueOf(authorization));
        var jsonBody = objectMapper.writeValueAsString(body);
        sendRequest(method,endpoint,jsonBody);
    }

    @Then("I should get {int} status code")
    public void the_response_status_code_should_be(int statusCode) {
        response.then().statusCode(statusCode);
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


}
