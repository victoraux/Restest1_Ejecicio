import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import static io.restassured.RestAssured.filters;

public abstract class BaseTest {

  //  private static final Logger logger = LogManager.getLogger(ReqResTest.class);

    //private static final Logger logger = LogManager.getLogger(ReqResTest.class);

    @BeforeClass

    public static void setup(){
       //  logger.info("Iniciando la configuracion");
        RestAssured.requestSpecification = defaultRequestSpecification();
       //  logger.info("Configuracion exitosa.");


        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
}

    private static RequestSpecification defaultRequestSpecification (){

        List<Filter> filters = new ArrayList<>();
        filters.add((Filter)new RequestLoggingFilter());
        filters.add((Filter) new ResponseLoggingFilter());
        filters.add((Filter) new AllureRestAssured());

        return new RequestSpecBuilder().setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
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