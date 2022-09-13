package de.honoka.test.various.old.p2;

import org.junit.Test;

public class FinalizeTest {
	
	@Test
	public void main() throws Exception {
		Util u = new Util();
		System.out.println("开启状态：" + u.isOpen());
		u = null;
		System.gc();
		/*int i = 0;
		while (i < 30) {
			Thread.sleep(1000);
			i++;
			System.out.println(i);
		}*/
	}
	
	private static class Util {
		private boolean open = true;
		
		public void close() {
			open = false;
			System.out.println("Util被关闭");
		}
		
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
			close();
		}
		
		public boolean isOpen() {
			return open;
		}
	}
}


