package de.honoka.test.spring.task;

import de.honoka.standard.task.Task;
import de.honoka.util.HonokaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j @Component @EnableScheduling
public class TaskImpl extends Task {
	
	@Resource
	private TaskImpl thisInstance;
	
	@Override
	public Task getAgentInstance() {
		return thisInstance;
	}
	
	@Override
	protected String initCron() {
		return "0/10 * * * * ?";
	}
	
	@Override
	protected String getName() {
		return null;
	}
	
	@Override
	public synchronized String run() throws Exception {
		return super.run();
	}
	
	@Override
	protected String task() throws Exception {
		return HonokaUtils.getSimpleDateFormat("normal").format(new Date()) +
				"\t任务执行\t" + getCron();
	}
}
