import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoTest {

    @Test
    public void test(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
        ;
    }
    @Test
    public void contentTypeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }
    @Test
    public void checkCounrtyInResponseBody(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .body("country",equalTo("United States"))
        ;
    }
    @Test
    public void test5(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
        ;
    }
    @Test
    public void test6(){
        given()
                .when()
                .pathParam("Country","us")
                .pathParam("Zipcode",90210)
                .log().uri()
                .get("http://api.zippopotam.us/{Country}/{Zipcode}")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)

        ;
    }
    @Test
    public void test7(){
        for (int i = 90210; i <= 90213 ; i++) {
            given()
                    .when()
                    .pathParam("Country", "us")
                    .pathParam("Zipcode", i)
                    .log().uri()
                    .get("http://api.zippopotam.us/{Country}/{Zipcode}")

                    .then()
                    .log().body() // log().all() all response
                    .statusCode(200)
                    .body("places.state", hasSize(1))
            ;
        }
    }
    @Test
    public void test8(){
        given()
                .when()
                .param("page",2)
                .log().uri()
                .get("http://gorest.co.in/public/v1/users")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .body("meta.pagination.page",equalTo(2))

        ;
    }
    @Test
    public void test9(){
        for (int i = 1; i <=10 ; i++) {
            given()
                    .when()
                    .param("page", i)
                    .log().uri()
                    .get("http://gorest.co.in/public/v1/users")

                    .then()
                    .log().body() // log().all() all response
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i))

            ;
        }
    }
    @Test
    public void test3(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .body("places.'place name'",hasItem("Dörtağaç Köyü"))
        ;
    }
    @Test
    public void test4(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body() // log().all() all response
                .statusCode(200)
                .body("places",hasSize(3))
        ;
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    void setup(){
        baseURI="http://gorest.co.in/public/v1";
        requestSpecification=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpecification=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void test10(){
        given()
                .when()
                .param("page",2)
                .spec(requestSpecification)
                .get("/users")

                .then()
                .spec(responseSpecification)
                .body("meta.pagination.page",equalTo(2))

        ;
    }

    @Test
    public void extractingJsonPath(){
        String placeName=
                given()
                .when()
                        .get("http://api.zippopotam.us/us/90210")

                .then()
                        .statusCode(200)
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt(){
        int limit=
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");
    }
    @Test
    public void extractingJsonPathList(){
        List<Integer> ids=
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().path("data.id")
                ;
        System.out.println("ids = " + ids);
        Assert.assertTrue(ids.contains(4040));
    }
    @Test
    public void extractingJsonPathResponseAll(){
        Response response =
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .extract().response()
                ;
        List<Integer> ids=response.path("data.id");
        List<String> names=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("response = " + response.prettyPrint());
        System.out.println("ids = " + ids);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingPOJO(){
        Location yer=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .extract().as(Location.class)

        ;
        System.out.println("yer.getPostCode() = " + yer.getPostCode());
    }
}

