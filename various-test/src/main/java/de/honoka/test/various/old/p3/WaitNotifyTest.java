package de.honoka.test.various.old.p3;

import org.junit.Test;

import java.util.Random;

@SuppressWarnings("ALL")
public class WaitNotifyTest {
	
	static class Message {
		
		String msg;
		
		public Message(String msg) {
			this.msg = msg;
		}
	}
	
	static class MessageContainer {
		
		volatile Message msg;
		
		synchronized void sendMsg(String msg) {
			this.msg = new Message(msg);
			notifyAll();
			//resumeAllWaiting();
		}
	}
	
	static class MessageMonitor {
		
		MessageContainer container;
		
		Message lastMsg;
		
		MessageMonitor(MessageContainer container) {
			this.container = container;
		}
		
		//List<Thread> waitingThreads = new LinkedList<>();
		
		Message waitForMsg() throws Exception {
			//if(msg != null) return msg;
			//System.out.println(Thread.currentThread().getName() + "等待消息");
			//for(; ; ) {
			//	wait();
				//Thread currentThread = Thread.currentThread();
				//waitingThreads.add(currentThread);
				//currentThread.suspend();
			//	if(msg != null) {
			//		try {
			//			return msg;
			//		} finally {
			//			msg = null;
			//		}
			//	}
			//}
			lastMsg = container.msg;
			while (lastMsg == container.msg) {
				synchronized (container) {
					container.wait();
				}
			}
			return container.msg;
		}
		
		//void resumeAllWaiting() {
		//	System.out.println("等待数：" + waitingThreads.size());
		//	while (!waitingThreads.isEmpty()) {
		//		waitingThreads.get(0).resume();
		//		waitingThreads.remove(0);
		//	}
		//	this.msg = null;
		//}
	}
	
	@Test
	public void test() throws Exception {
		MessageContainer container = new MessageContainer();
		Runnable action = () -> {
			MessageMonitor monitor = new MessageMonitor(container);
			try {
				for(; ; ) {
					Message msg = monitor.waitForMsg();
					System.out.println(Thread.currentThread().getName() +
							"得到新消息：" + msg.msg);
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
			container.sendMsg("hello! 这是第" + i + "条消息");
		}
	}
}
