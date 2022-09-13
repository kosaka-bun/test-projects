package de.honoka.test.various.old.p1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Clock extends JFrame implements ActionListener {
    private JLabel label = new JLabel(String.format("%tY年%<tm月%<td日 %<tT",new Date()));
    private Timer timer = new Timer(50,this);    //创建一个定时器，并注册当前对象为监视器
    public static void main(String[] args) {
        Clock frm = new Clock("小时钟");
        frm.setBounds(100,100,380,80);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }
    public Clock(String s) {
        setTitle(s);
        setLayout(new FlowLayout());
        label.setFont(new Font("微软雅黑",Font.PLAIN,25));      	//设置字体
        add(label);
        timer.start();   	//定时器开始
    }
    public void actionPerformed(ActionEvent e) {
        String s = String.format("%tY年%<tm月%<td日 %<tT",new Date());
        label.setText(s);
    }
}