import de.honoka.qqrobot.base.system.RobotAttributes;
import de.honoka.qqrobot.framework.Robot;
import de.honoka.util.HonokaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class AllTest {
	
	@Test
	public void test1() {
		robot.startup();
		attributes.isEnabled = true;
		for(;;) {
			HonokaUtils.threadSleep(5000);
		}
	}
	
	@Resource
	private RobotAttributes attributes;
	
	@Resource
	private Robot robot;
}
