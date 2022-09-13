package de.honoka.test.various.old.p3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopupMenuTest {
	
	static {
		//设置本机系统外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("测试窗口");
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		// 直接在内容面板上添加鼠标监听器
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 鼠标点击（按下并抬起）
				
				// 如果是鼠标右键，则显示弹出菜单
				if (e.isMetaDown()) {
					showPopupMenu(e.getComponent(), e.getX(), e.getY());
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// 鼠标按下
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// 鼠标释放
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// 鼠标进入组件区域
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// 鼠标离开组件区域
			}
		});
		
		jf.setContentPane(panel);
		jf.setVisible(true);
		
		JWindow jw = new JWindow();
		jw.setSize(100, 100);
		jw.setLocationRelativeTo(null);
		jw.setBackground(Color.BLACK);
		//jw.setVisible(true);
		jw.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("focusGained");
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("focusLost");
			}
		});
	}
	
	public static void showPopupMenu(Component invoker, int x, int y) {
		// 创建 弹出菜单 对象
		JPopupMenu popupMenu = new JPopupMenu();
		
		// 创建 一级菜单
		JMenuItem copyMenuItem = new JMenuItem("复制");
		JMenuItem pasteMenuItem = new JMenuItem("粘贴");
		JMenu editMenu = new JMenu("编辑");   // 需要 添加 二级子菜单 的 菜单，使用 JMenu
		JMenuItem fileMenu = new JMenuItem("文件");
		
		// 创建 二级菜单
		JMenuItem findMenuItem = new JMenuItem("查找");
		JMenuItem replaceMenuItem = new JMenuItem("替换");
		// 添加 二级菜单 到 "编辑"一级菜单
		editMenu.add(findMenuItem);
		editMenu.add(replaceMenuItem);
		
		// 添加 一级菜单 到 弹出菜单
		popupMenu.add(copyMenuItem);
		popupMenu.add(pasteMenuItem);
		popupMenu.addSeparator();       // 添加一条分隔符
		popupMenu.add(editMenu);
		popupMenu.add(fileMenu);
		
		// 添加菜单项的点击监听器
		copyMenuItem.addActionListener(e -> System.out.println("复制 被点击"));
		findMenuItem.addActionListener(e -> System.out.println("查找 被点击"));
		// ......
		
		// 在指定位置显示弹出菜单
		popupMenu.show(invoker, x, y);
	}
}
