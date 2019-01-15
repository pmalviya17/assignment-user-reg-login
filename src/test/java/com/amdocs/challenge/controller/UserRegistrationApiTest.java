package com.amdocs.challenge.controller;

import com.uxpsystems.assignment.UserRegistrationLoginApplication;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.model.UserStatus;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRegistrationLoginApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRegistrationApiTest {

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testGetUser() {
        Response response = RestAssured.when().get("/users");

        assertEquals("200 must be returned", HttpStatus.NOT_FOUND.value(), response.statusCode());
    }
    @Test
    public void testAddUser() {
        Headers headers = populateAdminHeaders();
        Response response = RestAssured.given()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(this.createMockUser())
                .when().post("/add");

        assertEquals("200 must be returned", HttpStatus.FORBIDDEN.value(), response.statusCode());
    }
    /**
     *
     * @return
     */
    private Headers populateAdminHeaders() {
        return new Headers(new Header("consumer-key", "admin"));
    }
    private User createMockUser() {
        return new User(Long.valueOf(1000), "amdocsuser001", "pritesh", "malviya",
                "pritesh.malviya@gmail.com", "amdocs-pass",
                "54e2f928-9fce-4035-8e83-cf6dbc79dcdc", UserStatus.ACTIVATED,
                new Date(), new Date());
    }
}
