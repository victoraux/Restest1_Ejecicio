import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import static io.restassured.RestAssured.filters;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class ReqResTest extends BaseTest{

  /*private static final Logger logger = LogManager.getLogger(ReqResTest.class);

   @Before

    public void setup(){
     // logger.info("Iniciando la configuracion");
      // logger.info("Configuracion exitosa");
       RestAssured.requestSpecification = defaultrequestSpecification();

   RestAssured.baseURI = "https://reqres.in";
    RestAssured.basePath = "/api";


}*/





    @Test
    public void loginTest() {

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}"
                )
                .post("https://reqres.in/api/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void getSingleUserTest() {

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .get("https://reqres.in/api/users/2")
                .then()
                .log()
                .all()
                .statusCode(200)
                //resultado del response date, id = 2
                .body("data.id", equalTo(2));

        // System.out.println(response);
    }

    @Test

    public void DeleteUserTest() {
        //String response = RestAssured
        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .delete("https://reqres.in/api/users/2")
                .then()
                .log()
                .all()
                .statusCode(204);
        // System.out.println();


    }

    @Test
    public void patchUserTest() {

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "     \"nombre\":  \"morfeo\" ,\n" +
                        "     \"trabajo\":  \"residente de zion\" \n" +
                        "}")
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(200);


    }

    @Test

    public void putUserTest() {

        String nameUpdated = given()
                .when()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "     \"nombre\":  \"morfeo\" ,\n" +
                        "     \"trabajo\":  \"residente de zion\" \n" +
                        "}")

                .put("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getString("nombre");
        assertThat(nameUpdated, equalTo("morfeo"));

    }

    @Test

    public void getAllUsersTest2() {

        String response = given()
                .when()
                .get("https://reqres.in/api/users?page=2").then()
                .extract().body().asString();

        int page = from(response).get("page");
        int totalPage = from(response).get("total:pages");
        int idFirstUser = from(response).get("data[0].id");

        System.out.println("page: " + page);
        //   System.out.println("total page: " + totalPage);
        // System.out.println("id first user: " + idFirstUser);


    }
@Test
    public void registerUserTest () {

        CreateUserRequest user = new CreateUserRequest();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

    CreateUserResponse userResponse = given()
                .when()
                .body(user)
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(CreateUserResponse.class);

        assertThat(userResponse.getId(), equalTo(4));
        assertThat(userResponse.getToken(), equalTo("QpwL5tke4Pnpja7X4"));

    }

    private RequestSpecification  defaultrequestSpecification(){

        List<Filter> filters = new ArrayList<>();
        filters().add(new RequestLoggingFilter());
        filters().add(new ResponseLoggingFilter());

        return new RequestSpecBuilder().setBaseUri("Https://resqres.in")
                .setBasePath("/api")
                .addFilters(filters())
                .setContentType(ContentType.JSON).build();
    }

    private RequestSpecification prodRequestSpecification(){
       return new RequestSpecBuilder().setBaseUri("https://prod.reqres.in")
               .setBasePath("/api")
               .setContentType(ContentType.JSON).build();

    }

    public ResponseSpecification defaultResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();

    }
}