package ru.tinkoff.qa.apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tinkoff.qa.apitestmodels.*;
import java.util.Arrays;

public class PetsApiTests {
    UserRequest userRequest;
    @BeforeEach
    public void init() {
        userRequest = new UserRequest();
        userRequest.setId(1);
        Category category = new Category();
        category.setId(0);
        category.setName("cat");
        userRequest.setCategory(category);
        TagsItem tags = new TagsItem();
        tags.setId(2);
        tags.setName("kitty");
        userRequest.setTags(Arrays.asList(tags));
        userRequest.setName("Sweftie");
        userRequest.setPhotoUrls(Arrays.asList("string"));
        userRequest.setStatus("available");

    }

    @Test
    public void postTest() {
        UserResponse userResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .post("https://petstore.swagger.io/v2/pet")
                .as(UserResponse.class);
        Assertions.assertEquals("Sweftie", userResponse.getName(),
                "Check post pet name");
    }

    @Test
    public void postCodeTest() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .post("https://petstore.swagger.io/v2/pet")
                .then().statusCode(200);
    }

    @Test
    public void getTest() {
        UserResponse userResponse = RestAssured.given()
                .get("https://petstore.swagger.io/v2/pet/"
                        + userRequest.getId())
                .then()
                .statusCode(200)
                .extract().response()
                .as(UserResponse.class);
        Assertions.assertEquals(userRequest.getId(), userResponse.getId(),
                "Check get pet id");
    }

    @Test
    public void putTest() {
        UserResponse userResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .put("https://petstore.swagger.io/v2/pet")
                .as(UserResponse.class);
        Assertions.assertEquals("available", userResponse.getStatus(),
                "Check put status");
    }

    @Test
    public void deleteCodeTest() {

        RestAssured.given()
                .delete("https://petstore.swagger.io/v2/pet/"
                        + userRequest.getId())
                .then()
                .statusCode(200);
    }
}