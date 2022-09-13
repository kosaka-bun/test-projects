package de.honoka.test.spring.task;

import de.honoka.standard.task.TaskAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class TaskAspectImpl implements TaskAspect {
	
	/**
	 * 在任务方法未成功执行时
	 */
	@AfterThrowing(value = "tasksPointcut()", throwing = "t")
	public void afterTaskThrowing(JoinPoint joinPoint, Throwable t) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		System.out.println(className + "未成功执行");
	}
	
	/**
	 * 在任务方法返回后
	 */
	//AfterReturning在After之后执行
	@AfterReturning(value = "tasksPointcut()", returning = "reply")
	public void afterTask(JoinPoint joinPoint, String reply) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		System.out.println(className + "执行完成，返回的信息是：\n" + reply);
	}
	
	/**
	 * 在每一个任务方法执行前
	 */
	@Before("tasksPointcut()")
	public void beforeTask(JoinPoint joinPoint) {
		//获取被切方法所属的对象原本的类的简单类名
		String className = joinPoint.getTarget().getClass().getSimpleName();
		System.out.println(className + "开始执行");
	}

	//task包及子包下任何一个类下的无参、返回值为String的run方法
	@Pointcut("execution(String de.honoka.test..*.run())")
	public void tasksPointcut() {}
}
