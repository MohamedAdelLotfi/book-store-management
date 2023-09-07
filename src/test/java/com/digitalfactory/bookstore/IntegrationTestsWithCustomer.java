package com.digitalfactory.bookstore;

import com.digitalfactory.bookstore.domain.BookCategory;
import com.digitalfactory.bookstore.web.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureTestDatabase
@Slf4j
public class IntegrationTestsWithCustomer {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setup() {
        RestAssured.port = this.port;
        token = given()
                .contentType(ContentType.JSON)
                .body(AuthenticationRequest.builder().username("user").password("password").build())
                .when().post("/auth/login")
                .andReturn().jsonPath().getString("token");
        log.debug("Got token:" + token);
    }

    @Test
    public void getAllBookCategories() throws Exception {
        given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/api/book-categories")

                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testSaveBookCategory() throws Exception {
        given()

            .contentType(ContentType.JSON)
            .body(BookCategory.builder().name("test").build())

        .when()
            .post("/v1/api/book-category")

        .then()
            .statusCode(401);
    }

    @Test
    public void testSaveWithAuthNotRole() throws Exception {

        given()
            .header("Authorization", "Bearer "+token)
            .contentType(ContentType.JSON)
            .body(BookCategory.builder().name("test").build())

        .when()
            .post("/v1/api/book-category")

        .then()
            .statusCode(403);
    }

    @Test
    public void testSaveWithInvalidAuth() throws Exception {

        given()
            .header("Authorization", "Bearer "+"invalidtoken")
            .contentType(ContentType.JSON)
            .body(BookCategory.builder().name("test").build())

        .when()
            .post("/v1/api/book-category")
            
        .then()
            .statusCode(401);
    }
    
}
