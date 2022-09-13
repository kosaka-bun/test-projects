package de.honoka.test.various.old.p4;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.honoka.sdk.util.file.FileUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Iterator;
import java.util.Objects;

public class GsonTest {
	
	@Test
	@SneakyThrows
	public void test1() {
		String jsonStr = FileUtils.urlToString(Objects.requireNonNull(
				JsonApiTest.class.getResource("/JsonApiTest/2.json")));
		JsonObject jo = JsonParser.parseString(jsonStr).getAsJsonObject();
		System.out.println(jo);
		JsonArray items = jo.getAsJsonObject("data")
				.getAsJsonArray("items");
		for(Iterator<JsonElement> iterator = items.iterator();
			iterator.hasNext(); ) {
			JsonObject item = iterator.next().getAsJsonObject();
			if(item.get("password").getAsString().contains("p")) {
				iterator.remove();
			}
		}
		System.out.println(jo);
	}
}
