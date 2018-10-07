package com.example.circle;

/**
 * Created by liuan on 2018/2/5.
 */

public class Caculate {

    public static void main(String[] arg) {
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                int result = i + j;
                System.out.print(i + j + (result >= 10 || i >= 10 ? "  " : "   "));
                if (j == 11) {
                    System.out.println();
                }
            }
        }
    }
}
