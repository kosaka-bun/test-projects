package de.honoka.test.various.old.p3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class SystemTrayTest {
	
	static {
		//设置本机系统外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SystemTray tray = SystemTray.getSystemTray();
		// 获取图片所在的URL
		URL url = SystemTrayTest.class.getResource("/img/tomcat.jpg");
		// 实例化图像对象
		ImageIcon icon = new ImageIcon(url);
		// 获得Image对象
		Image image = icon.getImage();
		// 创建托盘图标
		TrayIcon trayIcon = new TrayIcon(image);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 如果是鼠标右键，则显示弹出菜单
				if (e.getButton() != MouseEvent.BUTTON3) return;
				showMenu(e.getPoint());
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
			
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			
			}
		});
		try {
			tray.add(trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void showMenu(Point location) {
		int x = (int) (location.getX() / 1.25);
		int y = (int) (location.getY() / 1.25);
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new JMenuItem("Open"));
		popupMenu.add(new JMenuItem("Exit"));
		//System.out.println(popupMenu.getSize());
		//popupMenu.setVisible(true);
		JWindow jWindow = new JWindow();
		jWindow.add(popupMenu);
		jWindow.setSize(0, 0);
		jWindow.setLocation(0, 0);
		jWindow.setVisible(true);
		FocusListener focusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("获得焦点");
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("失去焦点");
			}
		};
		//popupMenu.addFocusListener(focusListener);
		jWindow.addFocusListener(focusListener);
		popupMenu.show(jWindow, x, y);
	}
}
