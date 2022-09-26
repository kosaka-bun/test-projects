package de.honoka.test.various.old.p3;

import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.UUID;

public class JTextPaneTest {

	@SneakyThrows
	static void test1() {
		JFrame frame = new JFrame();
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane();

		//region textPane
		textPane.setBackground(Color.BLACK);
		textPane.setForeground(Color.LIGHT_GRAY);
		textPane.setMargin(new Insets(8, 8, 8, 8));
		textPane.setFont(new Font("Microsoft YaHei Mono",
				Font.PLAIN, 18));
		textPane.setEditable(false);
		//endregion
		//region scrollPane
		scrollPane.setViewportView(textPane);
		scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame.add(scrollPane);
		//endregion
		//region frame
		frame.setTitle("Test");
		frame.setMinimumSize(new Dimension(1000, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		//endregion
		frame.setVisible(true);

		//文档
		StyledDocument doc = textPane.getStyledDocument();
		doc.insertString(doc.getLength(), "普通文本\n", new SimpleAttributeSet());
		SimpleAttributeSet set = new SimpleAttributeSet();
		System.err.println("对照色ABCD");
		StyleConstants.setForeground(set, new Color(255, 110, 110, 255));
		String[] strs = {
				"格式文本",
				"特↑殊↓字←符→",
				"абвгдеёжзийклмнопрстуфх",
				"△▽○◇□☆▷◁♤♡♢♧♣♦♥♠◀▶★■◆●▼▲",
				"☼☽♀☺◐☑√✔☜☝☞㏂㏘☛☟☚✘×☒◑☹♂☾☀▪•‥…∷※♩♫♪♬§♭♯♮㈱↖↑↗→㊣←↙↓↘",
				"あいうえおかきくけこアイウエオカキクケコ"
		};
		for(String str : strs) {
			doc.insertString(doc.getLength(), str + "\n", set);
		}
		System.out.println("-------------------------");
		System.out.println(textPane.getDocument().getText(0,
				textPane.getDocument().getLength()));
		System.out.println("-------------------------");
	}
}
