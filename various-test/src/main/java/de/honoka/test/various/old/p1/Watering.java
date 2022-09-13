package de.honoka.test.various.old.p1;

import java.util.Random;
import java.util.Scanner;

public class Watering {
    static int level = 1, nowExp = 0, maxExp = 100; //基础等级与经验
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int flag;
        while (true) {
            System.out.print("请输入1浇水：");
            try {
                flag = sc.nextInt();
            } catch (Exception e) {
                flag = 0;
                sc = new Scanner(System.in);
            }
            if(flag == 1) {
                Random ra = new Random();
                int getExp = ra.nextInt(101) + 50;  //生成50-150的随机数
                nowExp += getExp;
                System.out.println("浇水成功，你本次获得的经验为：" + getExp);
                if(nowExp >= maxExp) {
                    level++;
                    maxExp += 100;
                    nowExp = 0;
                    System.out.println("恭喜你已升到" + level + "级");
                }
                System.out.println("目前等级：" + "Lv" + level + " (" + nowExp + "/" + maxExp + ")");
            }
        }
    }
}
