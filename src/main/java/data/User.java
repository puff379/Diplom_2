package data;

import java.util.Random;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User generateUser() {
        String uniqueTag= String.valueOf(new Random().nextInt(99999999));
        String email = "autotests"+uniqueTag + "@ya.ru";
        String password = "autotests"+uniqueTag;
        String name = "autotests"+uniqueTag;

        return new User(email, password, name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
