package demo.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given() //
                .auth().preemptive().basic("tester", "tester") //
                .when().get("/hello")//
                .then() //
                .statusCode(200) //
                .body(is("Hello tester"));
    }

}