package de.honoka.test.various.old.p3;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("ALL")
public class WaitNotifyTest2 {
	
	static class MessageMonitor {
		
		volatile String msg;
		
		final Set<Thread> waitingThreads = getSet(), usingThreads = getSet();
		
		<T> Set<T> getSet() {
			return Collections.synchronizedSet(new HashSet<>());
		}
		
		String waitForMsg() {
			Thread thisThread = Thread.currentThread();
			waitingThreads.add(thisThread);
			LockSupport.park();
			try {
				return msg;
			} finally {
				usingThreads.remove(thisThread);
			}
		}
		
		void sendMsg(String msg) {
			while(!usingThreads.isEmpty());
			this.msg = msg;
			synchronized (waitingThreads) {
				usingThreads.addAll(waitingThreads);
				waitingThreads.clear();
				usingThreads.forEach(LockSupport::unpark);
			}
		}
	}
	
	@Test
	public void test() throws Exception {
		MessageMonitor monitor = new MessageMonitor();
		Runnable action = () -> {
			try {
				for(; ; ) {
					String msg = monitor.waitForMsg();
					System.out.println(Thread.currentThread().getName() +
							"得到新消息：" + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		Thread t1 = new Thread(action, "线程1");
		Thread t2 = new Thread(action, "线程2");
		t1.start();
		t2.start();
		Random ra = new Random();
		for(int i = 1; ; i++) {
			int seconds = ra.nextInt(5) + 1;
			System.out.println("等待" + seconds + "秒");
			Thread.sleep(seconds * 1000);
			System.out.println("主线程发送消息");
			monitor.sendMsg("hello! 这是第" + i + "条消息");
			Thread.sleep(100);
			i++;
			monitor.sendMsg("hello! 这是第" + i + "条消息");
		}
	}
}
