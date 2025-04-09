package com.wallstreetcn.global.utils;

/**
 * Created by Leif Zhang on 2017/4/20.
 * Email leifzhanggithub@gmail.com
 */

public class FibonacciUtil {

    // 递归实现方式
    public static long fibonacci(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

}
