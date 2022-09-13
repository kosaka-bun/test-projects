package de.honoka.test.various.old.p4;

import de.honoka.sdk.json.api.JsonArray;
import de.honoka.sdk.json.api.JsonMaker;
import de.honoka.sdk.json.api.JsonObject;
import de.honoka.sdk.json.gson.GsonJsonArray;
import de.honoka.sdk.json.gson.GsonJsonMaker;
import de.honoka.sdk.json.gson.GsonJsonObject;
import de.honoka.sdk.util.file.FileUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonApiTest {
	
	//@Test
	public void test5() {
		JsonObject jo = newJsonObject();
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
		jo.add("key8", newJsonObject().add("key1.1", 0).add("key1.2", 1));
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
		JsonArray<Integer> array = newJsonArray(jsonStr, Integer.class);
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
				JsonApiTest.class.getResource("/JsonApiTest/2.json")));
		//JsonArray<JsonObject> array = new GsonJsonArray<>(jsonStr, JsonObject.class);
		//for(JsonObject jo : array) {
		//	System.out.println(jo);
		//	System.out.println(new Gson().fromJson(jo.toString(), TestUser.class));
		//}
		JsonArray<TestUser> array = newJsonArray(jsonStr, TestUser.class);
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
		JsonObject jo = newJsonObject();
		jo.add("a", 1);
		jo.add("b", true);
		System.out.println(jo);
		System.out.println(newJsonMaker().arbitrary(
				"json", jo, "c", "3", "d", "4", "e", "5"
		));
	}
	
	//@Test
	public void test1() {
		String jsonStr = FileUtils.urlToString(Objects.requireNonNull(
				JsonApiTest.class.getResource("/JsonApiTest/1.json")));
		JsonObject jo = newJsonObject(jsonStr);
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
	
	//region gson
	JsonObject newJsonObject() {
		return new GsonJsonObject();
	}

	JsonObject newJsonObject(String jsonStr) {
		return new GsonJsonObject(jsonStr);
	}

	<T> JsonArray<T> newJsonArray(Class<T> type) {
		return new GsonJsonArray<>(type);
	}

	<T> JsonArray<T> newJsonArray(String jsonStr, Class<T> type) {
		return new GsonJsonArray<>(jsonStr, type);
	}

	JsonMaker newJsonMaker() {
		return new GsonJsonMaker();
	}
	//endregion
	
	//region fastjson
	//JsonObject newJsonObject() {
	//	return new FastJsonObject();
	//}
	//
	//JsonObject newJsonObject(String jsonStr) {
	//	return new FastJsonObject(jsonStr);
	//}
	//
	//<T> JsonArray<T> newJsonArray(Class<T> type) {
	//	return new FastJsonArray<>(type);
	//}
	//
	//<T> JsonArray<T> newJsonArray(String jsonStr, Class<T> type) {
	//	return new FastJsonArray<>(jsonStr, type);
	//}
	//
	//JsonMaker newJsonMaker() {
	//	return new FastJsonMaker();
	//}
	//endregion
}
