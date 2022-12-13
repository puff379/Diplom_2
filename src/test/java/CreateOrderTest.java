import client.OrderClient;
import client.UserClient;
import data.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Variables;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateOrderTest {
    String authToken;
    User user;
    OrderClient orderClient;
    UserClient userClient;
    String correctIngredients = "{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa70\"]\n}";
    String emptyIngredients = "{\n\"ingredients\": []\n}";
    String wrongIngredients = "{\n\"ingredients\": [\"bluhbluhbluh\",\"bluhbluhbluhbluh\"]\n}";


    @Before
    public void setUp() {
        user = User.generateUser();
        orderClient = new OrderClient();
        userClient = new UserClient();
        Response createUserResponse = userClient.createUser(user);
        authToken = createUserResponse.path("accessToken");
    }

    @Test
    public void createOrderIngredientsAuthTest() {
        Response response = orderClient.createOrderWithToken(authToken.substring(7), correctIngredients);
        assertThat("Номер заказа null", response.path("order.number"), notNullValue());
        assertThat("Ответ не содержит параметра success со значением true", response.path("success"), equalTo(true));
    }

    @Test
    public void createOrderWithIngredientsNoAuthTest() {
        Response response = orderClient.createOrderWithoutToken(correctIngredients);
        assertThat("Номер заказа null", response.path("order.number"), notNullValue());
        assertThat("Ответ не содержит параметр success со значением true", response.path("success"), equalTo(true));
    }

    @Test
    public void createOrderNoIngredientsWithAuthTest() {
        Response response = orderClient.createOrderWithToken(authToken.substring(7), emptyIngredients);
        assertThat("Код ответа " + response.getStatusCode(), response.statusCode(), equalTo(400));
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Проблемы в ответном message", response.path("message"), equalTo(Variables.MSG_INGREDIENT_ID_MUST_BE_PROVIDED));
    }

    @Test
    public void createOrderNoAuthNoIngredientsTest() {
        Response response = orderClient.createOrderWithoutToken(emptyIngredients);
        assertThat("Код ответа " + response.getStatusCode(), response.statusCode(), equalTo(400));
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Проблемы в ответном message", response.path("message"), equalTo(Variables.MSG_INGREDIENT_ID_MUST_BE_PROVIDED));
    }

    @Test
    public void createOrderAuthWrongIngredientsTest() {
        Response response = orderClient.createOrderWithToken(authToken.substring(7), wrongIngredients);
        assertThat("Код ответа " + response.getStatusCode(), response.statusCode(), equalTo(500));
    }

    @Test
    public void createOrderWrongIngredientsNoAuthTest() {
        Response response = orderClient.createOrderWithoutToken(wrongIngredients);
        assertThat("Код ответа " + response.getStatusCode(), response.statusCode(), equalTo(500));
    }

    @After
    public void cleanUp() {
        if (authToken != null) {
            userClient.deleteUser(authToken.substring(7));
        }
    }

}
