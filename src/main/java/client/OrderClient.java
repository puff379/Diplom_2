package client;

import io.restassured.response.Response;
import utils.Variables;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    public Response createOrderWithToken(String token, String ingredient) {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .body(ingredient)
                .post(Variables.ORDERS_ENDPOINT);
    }

    public Response getUserOrdersWithToken(String token) {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .get(Variables.ORDERS_ENDPOINT);
    }

    public Response createOrderWithoutToken(String ingredient) {
        return given()
                .spec(getSpec())
                .body(ingredient)
                .post(Variables.ORDERS_ENDPOINT);
    }

    public Response getUserOrdersWithoutToken() {
        return given()
                .spec(getSpec())
                .get(Variables.ORDERS_ENDPOINT);
    }
}
