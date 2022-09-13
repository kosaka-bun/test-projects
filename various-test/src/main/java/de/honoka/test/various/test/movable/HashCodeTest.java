package de.honoka.test.various.test.movable;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;

public class HashCodeTest {

    @Data
    @Accessors(chain = true)
    static class User {

        private Integer id;

        private String name;
    }

    static User newUser(Integer id, String name) {
        return new User()
                .setId(id)
                .setName(new String(
                        name.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8
                ));
    }

    public static void main(String[] args) {
        System.out.println(newUser(500, "test").hashCode());
        System.out.println(newUser(500, "test").hashCode());
        System.out.println(newUser(600, "test2").hashCode());
        System.out.println(newUser(600, "test2").hashCode());
    }
}
