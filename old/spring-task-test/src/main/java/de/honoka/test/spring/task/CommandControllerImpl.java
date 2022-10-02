package de.honoka.test.spring.task;

import de.honoka.qqrobot.base.controller.Command;
import de.honoka.qqrobot.base.controller.CommandController;
import de.honoka.qqrobot.base.controller.CommandMethodArgs;
import de.honoka.qqrobot.base.system.RobotAttributes;
import de.honoka.qqrobot.base.system.SessionManager;
import de.honoka.qqrobot.base.system.bean.RobotSession;
import de.honoka.qqrobot.framework.Framework;
import de.honoka.standard.task.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;

@SuppressWarnings("unused")
@Component
public class CommandControllerImpl implements CommandController {
	
	@Command("执行任务")
	public String cmd6(CommandMethodArgs args) throws Exception {
		return taskImpl.runInAdvance();
	}
	
	@Command("任务状态")
	public String cmd5(CommandMethodArgs args) {
		if(taskImpl.isRunning())
			return "任务运行中";
		else
			return "任务已停止";
	}
	
	@Command("开始任务")
	public String cmd4(CommandMethodArgs args) {
		taskImpl.start();
		return "任务已开始";
	}
	
	@Command("停止任务")
	public String cmd3(CommandMethodArgs args) {
		taskImpl.cancel();
		return "任务已停止";
	}
	
	@Command(value = "更改cron", argsNum = 6)
	public String cmd2(CommandMethodArgs args) {
		String[] a = args.args;
		String cron = String.format("%s %s %s %s %s %s",
				a[0], a[1], a[2], a[3], a[4], a[5]);
		taskImpl.restart(cron);
		return "cron已修改为：" + cron;
	}
	
	@Command("命令1")
	public String cmd1(CommandMethodArgs args) {
		return "调用了命令1";
	}
	
	@Resource
	private TaskImpl taskImpl;
	
	@Resource
	private Framework framework;
}
