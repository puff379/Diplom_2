import client.OrderClient;
import client.UserClient;
import data.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Variables;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrderTest {
    String authToken;
    UserClient userClient;
    OrderClient orderClient;
    User user;
    String correctIngredients = "{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa75\",\"61c0c5a71d1f82001bdaaa70\"]\n}";

    @Before
    public void setUp() {
        user = User.generateUser();
        orderClient = new OrderClient();
        userClient = new UserClient();

        Response createUserResponse = userClient.createUser(user);
        authToken = createUserResponse.path("accessToken");
        orderClient.createOrderWithToken(authToken.substring(7), correctIngredients);
    }
    
    @Test
    public void getOrdersFromUserWithAuth() {
        Response response = orderClient.getUserOrdersWithToken(authToken.substring(7));
        assertThat("Ответа не содержит параметра success со значением true", response.path("success"), equalTo(true));
        assertThat("Код ответа "+response.statusCode(), response.statusCode(), equalTo(200));
    }

    @Test
    public void getOrdersFromUserNoAuth() {
        Response response = orderClient.getUserOrdersWithoutToken();
        assertThat("number не null", response.path("order.number"), nullValue());
        assertThat("Ответ не содержит параметра success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа "+response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", response.path("message"), equalTo(Variables.MSG_UNAUTHORIZED));
    }
    @After
    public void cleanUp() {
        if (authToken != null) {
            userClient.deleteUser(authToken.substring(7));
        }
    }
}

