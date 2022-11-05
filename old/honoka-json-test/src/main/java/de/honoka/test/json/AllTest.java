package de.honoka.test.json;

import de.honoka.sdk.json.api.JsonArray;
import de.honoka.sdk.json.api.JsonObject;
import de.honoka.sdk.json.api.util.JsonMaker;
import de.honoka.sdk.util.file.FileUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllTest {

    @Test
    public void test7() {
        TestUser user1 = new TestUser(1, "abc", "def");
        TestUser user2 = new TestUser(2, "ghi", "jkl");
        System.out.println(JsonObject.of(user1));
        List<TestUser> userList = List.of(user1, user2);
        JsonArray<TestUser> jsonArray = JsonArray.of(userList, TestUser.class);
        System.out.println(jsonArray);
        for(TestUser user : jsonArray) {
            System.out.println(user);
        }
    }

    //@Test
    public void test6() {
        //JsonConfig.get().setPretty(true);
        //JsonConfig.get().setCamelCase(true);
        JsonObject jo = JsonObject.of();
        jo.add("a", 1).add("b", 2);
        jo.add("obj", new TestUser(1, "abc", "def"));
        System.out.println(jo.toPrettyString());
        System.out.println(jo);
        System.out.println(jo.getJsonObject("obj")
                .toObject(TestUser.class));
    }

    //@Test
    public void test5() {
        JsonObject jo = JsonObject.of();
        System.out.println(jo);
        jo.add("key1", 1);
        System.out.println(jo);
        jo.add("key2", 1.5);
        System.out.println(jo);
        jo.add("key3", true);
        System.out.println(jo);
        jo.add("key4", "string");
        System.out.println(jo);
        jo.add("key5", new TestUser(1, "user1", "pass1"));
        System.out.println(jo);
        jo.add("key6", null);
        System.out.println(jo);
        jo.add("key7", List.of(1, 2));
        System.out.println(jo);
        jo.add("key8", JsonObject.of().add("key1.1", 0).add("key1.2", 1));
        System.out.println(jo);
        jo.add("key9", List.of(
                new TestUser(2, "user2", "pass2"),
                new TestUser(3, "user3", "pass3")
        ));
        System.out.println(jo);
        jo.add("key10", Map.of("mk1", "mv1", "mk2", "mv2"));
        System.out.println(jo);
        jo.forEach((key, value) -> {
            if(value == null) return;
            System.out.print(value.getClass().getSimpleName() + ", ");
        });
        System.out.println();
    }

    //@Test
    public void test4() {
        String jsonStr = "[1,2,3,4,5,6,7,8,9]";
        JsonArray<Integer> array = JsonArray.of(jsonStr, Integer.class);
        System.out.println(array);
        array.add(10);
        System.out.println(array);
        array.addAll(List.of(11, 12));
        System.out.println(array);
        array.remove(1);
        System.out.println(array);
        array.removeAll(List.of(3, 4, 5, 6, 7));
        System.out.println(array);
        array.retainAll(List.of(10, 12));
        System.out.println(array);
        array.clear();
        System.out.println(array);
    }

    //@Test
    public void test3() {
        String jsonStr = FileUtils.urlToString(Objects.requireNonNull(
                AllTest.class.getResource("/2.json")));
        jsonStr = JsonObject.of(jsonStr).getJsonArray("data.items",
                JsonObject.class).toString();
        JsonArray<TestUser> array = JsonArray.of(jsonStr, TestUser.class);
        array.add(new TestUser(9, "ab", "abc"));
        for(TestUser testUser : array) {
            System.out.println(testUser);
        }
    }

    public static class TestUser {
        public int id;
        public String userName, password;

        public TestUser(int id, String userName, String password) {
            this.id = id;
            this.userName = userName;
            this.password = password;
        }

        @Override
        public String toString() {
            return "TestUser{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    //@Test
    public void test2() {
        JsonObject jo = JsonObject.of();
        jo.add("a", 1);
        jo.add("b", true);
        System.out.println(jo);
        System.out.println(JsonMaker.arbitrary(
                "json", jo, "c", "3", "d", "4", "e", "5"
        ));
    }

    //@Test
    public void test1() {
        String jsonStr = FileUtils.urlToString(Objects.requireNonNull(
                AllTest.class.getResource("/1.json")));
        JsonObject jo = JsonObject.of(jsonStr);
        System.out.println(jo.getJsonArray("data", Integer.class));
        System.out.println(jo.getJsonObject("level1"));
        System.out.println(jo.getJsonObject("level1.level2"));
        System.out.println(jo.getString("string"));
        System.out.println(jo.getString("level1.level2.level3.inner"));
        JsonArray<JsonArray<Integer>> array = jo.getJsonArray(
                "array", Integer.class);
        for(JsonArray<Integer> integers : array) {
            for(Integer integer : integers) {
                System.out.print(integer + ", ");
            }
        }
        System.out.println();
        JsonArray<JsonObject> array2 = jo.getJsonArray(
                "array2", JsonObject.class);
        for(JsonObject jo2 : array2) {
            System.out.println(jo2);
        }
        System.out.println(jo.keySet());
    }
}
