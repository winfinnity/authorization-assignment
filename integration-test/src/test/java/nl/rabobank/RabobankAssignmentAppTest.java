package nl.rabobank;

import jakarta.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RabobankAssignmentAppTest {

    @LocalServerPort
    private int port;

    private String uri;

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @Test
    void findAllCustomersTest() {
        given().log().all()
            .when()
            .get(uri + "/api/authorizations?grantee=piet")
            .then()
            .statusCode(200);
    }

}
