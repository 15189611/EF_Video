package com.charles.downvideo;

public class 递归 {
    //回溯
    public void task(int n) {
        if (n > 2) {
            task(n - 1);
        }
        System.out.println("n==" + n);
    }

    public int task2(int n) {
        if (n == 1) {
            return 1;
        }

        return task2(n - 1) * n;
        //                 1* 2 * 3    //传3
        //                 1* 2 * 3 * 4  //传4
        //                 1* 2 * 3 * 4 * 5  //传5

    }
}
