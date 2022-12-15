import client.UserClient;
import data.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Variables;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginUserTest {
    User user;
    UserClient userClient;
    String authToken;

    @Before
    public void setUp() {
        user = User.generateUser();
        userClient = new UserClient();
        userClient.createUser(user);
    }

    @Test
    public void authorizeWithCorrectLoginAndPassword() {
        Response response = userClient.loginUser(user);
        assertThat("Ответ не содержит параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Отсутствует accessToken", response.path("accessToken"), notNullValue());
        assertThat("Невалидны данные токена", response.path("accessToken"), containsString("Bearer"));
        assertThat("Ответ содержит неверный email", response.path("user.email"), equalTo(user.getEmail().toLowerCase()));
        assertThat("Ответ содержит неверное имя", response.path("user.name"), equalTo(user.getName()));
    }

    @Test
    public void authorizeWithIncorrectLoginAndPassword() {
        user.setEmail(user.getEmail() + "somerandomletters");
        user.setPassword(user.getPassword() + "somerandomletters");
        Response response = userClient.loginUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа "+response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", response.path("message"), equalTo(Variables.MSG_INCORRECT_LOGIN));
    }

    @Test
    public void authorizeWithIncorrectLoginAndCorrectPassword() {
        user.setEmail(user.getEmail() + "somerandomletters");
        Response response = userClient.loginUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа "+response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", response.path("message"), equalTo(Variables.MSG_INCORRECT_LOGIN));
    }

    @Test
    public void authorizeWithCorrectLoginAndIncorrectPassword() {
        user.setPassword(user.getPassword() + "somerandomletters");
        Response response = userClient.loginUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа "+response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Вернулось сообщение, не соответствующее ожидаемому", response.path("message"), equalTo(Variables.MSG_INCORRECT_LOGIN));
    }
    @After
    public void cleanUp() {
        if (authToken != null) {
            userClient.deleteUser(authToken.substring(7));
        }
    }
}
