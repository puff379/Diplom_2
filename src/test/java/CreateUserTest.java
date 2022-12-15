import client.UserClient;
import data.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Variables;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateUserTest {
    User user;
    UserClient userClient;
    String authToken;

    @Before
    public void setUp() {
        user = User.generateUser();
        userClient = new UserClient();
    }

    @Test
    public void createUserWithCorrectData() {
        Response response = userClient.createUser(user);
        assertThat("Ответ не содержит параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Отсутствует accessToken", response.path("accessToken"), notNullValue());
        assertThat("Невалидны данные токена", response.path("accessToken"), containsString("Bearer"));
    }

    @Test
    public void createAlreadyRegisteredUser() {
        userClient.createUser(user);
        Response response = userClient.createUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(403));
        assertThat("Ответ не содержит параметр message", response.path("message"), equalTo(Variables.MSG_EXISTS_USER));
    }

    @Test
    public void createUserWithoutEmail() {
        user.setEmail(null);
        Response response = userClient.createUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(403));
        assertThat("Ответа не содержит параметр message", response.path("message"), equalTo(Variables.MSG_REQUIRED_FIELDS));
    }

    @Test
    public void createUserWithoutPassword() {
        user.setPassword(null);
        Response response = userClient.createUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("\"Код ответа\" + response.statusCode()", response.statusCode(), equalTo(403));
        assertThat("Ответа не содержит параметр message", response.path("message"), equalTo(Variables.MSG_REQUIRED_FIELDS));
    }

    @Test
    public void createUserWithoutName() {
        user.setName(null);
        Response response = userClient.createUser(user);
        assertThat("Ответ не содержит параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(403));
        assertThat("Ответа не содержит параметр message", response.path("message"), equalTo(Variables.MSG_REQUIRED_FIELDS));
    }

    @After
    public void cleanUp() {
        if (authToken != null) {
            userClient.deleteUser(authToken.substring(7));
        }
    }
}
