package Campus;


import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {
    Cookies cookies;
    String countryID;
    Country country;

    @BeforeClass
    public void loginCampus() {
        baseURI = "https://test.mersys.io/";
        Map<String, String> credential = new HashMap<>();
        credential.put("username", "turkeyts");
        credential.put("password", "TechnoStudy123");
        credential.put("rememberMe", "true");
        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)
                        .when()
                        .post("auth/login")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomCode() {
        return RandomStringUtils.randomAlphabetic(3);
    }

    @Test
    public void createCountry() {
        country = new Country();
        country.setName(getRandomName());
        country.setCode(getRandomCode());
        countryID =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)
                        .when()
                        .post("school-service/api/countries")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("school-service/api/countries")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("The Country with Name \"" + country.getName() + "\" already exists."))
        ;
    }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry() {
        country.setId(countryID);
        country.setName("esterya");
        country.setCode("10");
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(country.getName()))
        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {
        given()
                .cookies(cookies)
                .pathParam("deleteID", countryID)
                .when()
                .delete("school-service/api/countries/{deleteID}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {
        given()
                .cookies(cookies)
                .pathParam("deleteID", countryID)
                .when()
                .delete("school-service/api/countries/{deleteID}")
                .then()
                .log().body()
                .statusCode(400)
        ;
    }
    @Test(dependsOnMethods = "deleteCountryNegative")
    public void updateCountryNegative() {
        country.setId(countryID);
        country.setName("esterya");
        country.setCode("10");
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("school-service/api/countries")
                .then()
                .log().body()
                .statusCode(400)
        ;
    }
}
