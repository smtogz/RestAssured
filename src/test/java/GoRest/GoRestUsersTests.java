package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    User newUser;
    int userId;
    @BeforeClass
    public void setup(){
        baseURI="https://gorest.co.in/public/v2/users";
    }
    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomEmail(){
        return RandomStringUtils.randomAlphabetic(8)+"@gmail.com";
    }
    @Test(enabled = false)
    public void createUserObject(){
        int userId=
        given()
                .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                .contentType(ContentType.JSON)
                .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                .log().uri()
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
        ;
        System.out.println("userId = " + userId);
    }
    @Test(enabled = false)
    public void createUserObjectWithMap(){
        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",getRandomName());
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");
        int userId=
                given()
                        .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .body(newUser)
                        .log().uri()
                        .when()
                        .post()
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
        System.out.println("userId = " + userId);
    }
    @Test(enabled = false)
    public void createUserObjectWithClass(){
        newUser=new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");
        userId=
                given()
                        .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .body(newUser)
                        .log().uri()
                        .when()
                        .post()
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
                ;
        System.out.println("userId = " + userId);
    }
    @Test
    public void createUserObjectWithClassExtract(){
        newUser=new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");
        userId=
                given()
                        .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .body(newUser)
                        .log().uri()
                        .when()
                        .post()
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("id")
        ;
        System.out.println("userId = " + userId);
    }
    @Test(dependsOnMethods = "createUserObjectWithClassExtract",priority = 1)
    public void getUserById(){
                given()
                        .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                        .pathParam("userId",userId)
                        .log().uri()
                        .when()
                        .get("/{userId}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("id")
        ;
        System.out.println("userId = " + userId);
    }
    @Test(dependsOnMethods = "createUserObjectWithClassExtract",priority = 2)
    public void updateUserById(){
        newUser.setName("esterello");
        given()
                .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                .pathParam("userId",userId)
                .contentType(ContentType.JSON)
                .body(newUser)
                .log().uri()
                .when()
                .put("/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userId))
        ;
    }
    @Test(dependsOnMethods = "createUserObjectWithClassExtract",priority = 3)
    public void deleteUserById(){

        given()
                .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                .pathParam("userId",userId)
                .log().uri()
                .when()
                .delete("/{userId}")
                .then()
                .log().body()
                .statusCode(204)
        ;
    }
    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative(){

        given()
                .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                .pathParam("userId",userId)
                .log().uri()
                .when()
                .delete("/{userId}")
                .then()
                .log().body()
                .statusCode(404)
        ;
    }
    @Test
    public void getUsers(){
        Response response =
        given()
                .header("Authorization","Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                .log().uri()
                .when()
                .get()
                .then()
                //.log().body()
                .statusCode(200)
                .extract().response()
        ;
        //System.out.println("response = " + response.prettyPrint());
        int idUser3path=response.path("[2].id");
        int idUser4Jsonpath=response.jsonPath().getInt("[2].id");
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser4Jsonpath = " + idUser4Jsonpath);

        User[] users=response.as(User[].class);
        System.out.println("Arrays.toString(users) = " + Arrays.toString(users));

        List<User> userList=response.jsonPath().getList("", User.class);
        System.out.println("userList = " + userList);

    }
    @Test
    public void getUsersV1() {
        Response response =
                given()
                        .header("Authorization", "Bearer 0327b6731e3aa5493491786c07e3e200c0b36e9f45b6c523475873d4bcd448d4")
                        .log().uri()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
                ;
        List<User> userList=response.jsonPath().getList("data", User.class);
        System.out.println("userList = " + userList);
        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
}
class User{
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}