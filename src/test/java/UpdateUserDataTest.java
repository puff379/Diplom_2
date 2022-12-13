import client.UserClient;
import data.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Variables;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateUserDataTest {
    User user;
    UserClient userClient;
    String authToken;

    @Before
    public void setUp() {
        user = User.generateUser();
        userClient = new UserClient();
        Response createUserResponse = userClient.createUser(user);
        authToken = createUserResponse.path("accessToken");
    }

    @Test
    public void сhangeAuthorizedUserData() {
        user.setEmail(user.getEmail() + "somerandomletters");
        user.setPassword(user.getPassword() + "somerandomletters");
        user.setName(user.getName() + "somerandomletters");
        Response response = userClient.changeUserDataWithToken(authToken.substring(7), user);
        assertThat("В ответе отсутствует параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(200));
        assertThat("Ответ содержит неверный email", response.path("user.email"), equalTo(user.getEmail().toLowerCase()));
        assertThat("Ответ содержит неверное имя", response.path("user.name"), equalTo(user.getName()));

    }

    @Test
    public void сhangeAuthorizedUserEmail() {
        user.setEmail(user.getEmail() + "somerandomletters");
        Response response = userClient.changeUserDataWithToken(authToken.substring(7), user);
        assertThat("В ответе отсутствует параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(200));
        assertThat("Ответ содержит неверный email", response.path("user.email"), equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    public void сhangeAuthorizedUserPassword() {
        user.setPassword(user.getPassword() + "somerandomletters");
        Response response = userClient.changeUserDataWithToken(authToken.substring(7), user);
        assertThat("В ответе отсутствует параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(200));
        assertThat("Ответ содержит неверный email", response.path("user.email"), equalTo(user.getEmail().toLowerCase()));
    }

    @Test
    public void сhangeAuthorizedUserName() {
        user.setName(user.getName() + "somerandomletters");
        Response response = userClient.changeUserDataWithToken(authToken.substring(7), user);
        assertThat("В ответе отсутствует параметр success со значением true", response.path("success"), equalTo(true));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(200));
        assertThat("Ответ содержит неверное имя", response.path("user.name"), equalTo(user.getName()));
    }

    @Test
    public void сhangeUnauthorizedUserData() {
        user.setEmail(user.getEmail() + "somerandomletters");
        user.setPassword(user.getPassword() + "somerandomletters");
        user.setName(user.getName() + "somerandomletters");
        Response response = userClient.changeUserDataWithoutToken(user);
        assertThat("В ответе отсутствует параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Ответ содержит текст ошибки, отличный от ожидаемого", response.path("message"), equalTo(Variables.MSG_UNAUTHORIZED));
    }

    @Test
    public void сhangeUnauthorizedUserEmail() {
        user.setEmail(user.getEmail() + "somerandomletters");
        Response response = userClient.changeUserDataWithoutToken(user);
        assertThat("В ответе отсутствует параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Ответ содержит текст ошибки, отличный от ожидаемого", response.path("message"), equalTo(Variables.MSG_UNAUTHORIZED));
    }

    @Test
    public void сhangeUnauthorizedUserPassword() {
        user.setPassword(user.getPassword() + "somerandomletters");
        Response response = userClient.changeUserDataWithoutToken(user);
        assertThat("В ответе отсутствует параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Ответ содержит текст ошибки, отличный от ожидаемого", response.path("message"), equalTo(Variables.MSG_UNAUTHORIZED));
    }

    @Test
    public void сhangeUnauthorizedUserName() {
        user.setName(user.getName() + "somerandomletters");
        Response response = userClient.changeUserDataWithoutToken(user);
        assertThat("В ответе отсутствует параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(401));
        assertThat("Ответ содержит текст ошибки, отличный от ожидаемого", response.path("message"), equalTo(Variables.MSG_UNAUTHORIZED));
    }

    @Test
    public void сhangeEmailOnAlreadyExist() {
        User userTwo = User.generateUser();
        userClient.createUser(userTwo);
        user.setEmail(userTwo.getEmail());
        Response response = userClient.changeUserDataWithToken(authToken.substring(7), user);
        assertThat("В ответе отсутствует параметр success со значением false", response.path("success"), equalTo(false));
        assertThat("Код ответа " + response.statusCode(), response.statusCode(), equalTo(403));
        assertThat("Ответ содержит текст ошибки, отличный от ожидаемого", response.path("message"), equalTo(Variables.MSG_EMAIL_ALREADY_EXISTS));

    }

    @After
    public void cleanUp() {
        if (authToken != null) {
            userClient.deleteUser(authToken.substring(7));
        }
    }
}
