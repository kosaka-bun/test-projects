import de.honoka.test.jni.JniTest;
import org.junit.Test;

public class AllTest {
	
	@Test
	public void test1() {
		System.out.println("file.encoding: " + System.getProperty("file.encoding"));
		JniTest test = new JniTest();
		test.start();
	}
}
