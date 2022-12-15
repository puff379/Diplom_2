package client;

import data.User;
import io.restassured.response.Response;
import utils.Variables;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(Variables.REG_ENDPOINT);
    }

    public Response loginUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(Variables.LOGIN_ENDPOINT);
    }

    public Response deleteUser(String token) {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .delete(Variables.USER_ENDPOINT);
    }

    public Response changeUserDataWithToken(String token, User user) {
        return given()
                .spec(getSpec())
                .auth().oauth2(token)
                .body(user)
                .patch(Variables.USER_ENDPOINT);
    }

    public Response changeUserDataWithoutToken(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .patch(Variables.USER_ENDPOINT);
    }

}
