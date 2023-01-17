import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Tasks {

    @Test
    public void test1(){
        User u1=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(User.class)
        ;
        System.out.println("u1 = " + u1);
    }
    @Test
    public void test2(){

                given()
                        .when()
                        .get("https://httpstat.us/203")
                        .then()
                        .log().body()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                ;
    }
    @Test
    public void test3(){

        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
        ;
        String text=
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .extract().body().asString()
        ;
        Assert.assertEquals(text,"203 Non-Authoritative Information");
    }
    @Test
    public void test4(){
        User u1=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(User.class)
                ;
        Assert.assertEquals(u1.getTitle(),"quis ut nam facilis et officia qui");
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;
    }
}
