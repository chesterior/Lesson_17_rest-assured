package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;

public class ReqresInTests {

    /* 1. make request to https://reqres.in/api/login
     body with {"email": "eve.holt@reqres.in", "password": "cityslicka"}
     2. get response {"token": "QpwL5tke4Pnpja7X4"}
     3. check token is "QpwL5tke4Pnpja7X4"
    */

    @Test
    void loginTest() {
        String body = "{\"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\"}";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void negativeLoginTest() {
        String body = "{\"email\": \"eve.holt@reqres.in\"}";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
