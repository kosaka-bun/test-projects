package de.honoka.test.spring.task;

import de.honoka.qqrobot.base.RobotComponents;
import de.honoka.qqrobot.base.RobotImpl;
import de.honoka.qqrobot.base.controller.CommandController;
import de.honoka.qqrobot.base.controller.CommandInvoker;
import de.honoka.qqrobot.base.service.SystemService;
import de.honoka.qqrobot.base.system.ExceptionReporter;
import de.honoka.qqrobot.base.system.MessageExecutor;
import de.honoka.qqrobot.base.system.RobotAttributes;
import de.honoka.qqrobot.base.system.SessionManager;
import de.honoka.qqrobot.framework.Framework;
import de.honoka.qqrobot.framework.Robot;
import de.honoka.qqrobot.framework.mirai.MiraiFramework;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
@Configuration
public class RobotComponentsImpl implements RobotComponents {

	@Bean
	@Override
	public Framework getFramework() {
		Properties props = new Properties();
		props.put("robot.qq", "xxxxxxxxxx");
		props.put("robot.password", "xxxxxxxx");
		props.put("framework.redirectLogs", "true");
		return new MiraiFramework(props);
	}

	@Bean
	@Override
	public SystemService getSystemService() {
		return new SystemService() {
			@Override
			public void init() {

			}

			@Override
			public void shutdown() {

			}
		};
	}

	@Bean
	@Override
	public SessionManager getSessionManager() {
		return new SessionManager();
	}

	@Bean
	@Override
	public ExceptionReporter getExceptionReporter() {
		return new ExceptionReporter() {
			@Override
			protected void logException(Throwable t) {

			}
		};
	}

	@Bean
	@Override
	public MessageExecutor getMessageExecutor(
			List<CommandController> controllers, RobotAttributes attributes) {
		return new MessageExecutor(controllers, attributes) {
			@Override
			public String commandPrefix() {
				return "🍋";
			}
		};
	}

	@Bean
	@Override
	public RobotAttributes getRobotAttributes() {
		return new RobotAttributes() {
			@Override
			public long adminQQ() {
				return 0;
			}

			@Override
			public long testGroup() {
				return 0;
			}

			@Override
			public long developingGroup() {
				return 0;
			}
		};
	}

	@Bean
	@Override
	public Robot robot() {
		return new RobotImpl();
	}
}
