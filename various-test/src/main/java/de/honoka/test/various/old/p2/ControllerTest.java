package de.honoka.test.various.old.p2;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerTest {
	
	RobotController rc = null;
	Map<String, Method> methodMap = new HashMap<>();
	
	@Test
	public void main() throws Exception {
		ControllerTest ct = new ControllerTest();
		//加载类和实例
		//Class.forName("de.honoka.test.various.old.p2.Controller");
		Class<Controller> controllerClass = Controller.class;
		ct.load(controllerClass.newInstance());
		//获取所有方法，提取含有Command注解的方法
		Method[] methods = controllerClass.getDeclaredMethods();
		System.out.println("所加载的控制器有如下Command方法：");
		for (Method m : methods) {
			String commandName;
			Command c = m.getAnnotation(Command.class);
			if(c != null) commandName = c.value();
			else continue;
			System.out.printf("方法名：%s\t命令名：%s\n", m.getName(), commandName);
			
			ct.methodMap.put(commandName, m);
		}
		//填入文字命令，调用方法
		String command = "命令5 abc 123";
		String[] commandParts = command.split(" ");
		//获取要调用的方法，和传入的参数个数
		Method m = ct.methodMap.get(commandParts[0]);
		int argsNumInput = commandParts.length - 1;
		int argsNum = m.getParameterCount();
		//传入的参数个数与对应的方法所需的参数个数不一致
		//参数过多或相同，不做特殊处理
		if(argsNumInput < argsNum) {	//参数不足
			System.out.println("提供的参数不足");
			return;
		}
		/* 由于不能以数组的方式传递可变长度参数，可以选择规定命令方法的参数个数不能超过多少个，
		或者所有控制器方法使用统一签名(String[] commandParts)，并在加载这些方法时验证这些方法的
		签名是否合法 */
		//根据要调用方法的参数个数判断调用语句
		switch (argsNum) {
			case 0:
				m.invoke(ct.rc);
				break;
			case 1:
				m.invoke(ct.rc, commandParts[1]);
				break;
			case 2:
				m.invoke(ct.rc, commandParts[1], commandParts[2]);
				break;
			case 3:
				m.invoke(ct.rc, commandParts[1], commandParts[2], commandParts[3]);
				break;
			case 4:
				m.invoke(ct.rc, commandParts[1], commandParts[2], commandParts[3], commandParts[4]);
				break;
			case 5:
				m.invoke(ct.rc, commandParts[1], commandParts[2], commandParts[3], commandParts[4], commandParts[5]);
				break;
			default:
				System.out.println("传入的参数过多");
		}
	}
	
	//加载RobotController
	void load(RobotController rc) {
		this.rc = rc;
	}
}

//指定了命令名的方法，提供命令名，可以调用对应命令名的方法
class Controller implements RobotController {
	
	@Command("命令1")
	void method1() {
		System.out.println("命令1被调用");
	}
	
	@Command("命令2")
	void method2(int arg) {
		System.out.println("命令2被调用：" + arg);
	}
	
	@Command("命令3")
	void method3(String arg) {
		System.out.println("命令3被调用：" + arg);
	}
	
	@Command("命令4")
	void method4(boolean arg) {
		System.out.println("命令4被调用：" + arg);
	}
	
	@Command("命令5")
	void method5(String arg1, Object arg2) {
		System.out.println(String.format("命令5被调用：%s\t%s", arg1, arg2));
	}
	
	void notCommandMethod() {
		System.out.println("非命令方法");
	}
}

//实现此接口，即可作为控制器使用
interface RobotController {
	//none
}

//此处声明的注解在包内均可访问
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)		//必须声明为runtime，否则获取Annotation时获取不到
@interface Command {
	//声明为value的注解值，在为目标添加注解填写注解值时，不用显式填写"value ="
	//@Command("abc") = @Command(value = "abc)
	//声明了默认值的注解值，在为目标添加注解时，可以不用指定该注解值，否则必须指定
	//int id() default -1;
	String value();		//此时在为目标添加注解时，必须显式指定value的值，否则无法通过编译
	//String[] parameters() default {};
}