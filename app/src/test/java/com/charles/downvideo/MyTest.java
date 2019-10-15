package com.charles.downvideo;

import org.junit.Before;
import org.junit.Test;

public class MyTest {
    递归 method;

    @Before
    public void init() {
        method = new 递归();

    }

    @Test
    public void test() {
        method.task(5);
    }

    @Test
    public void test2(){
        //阶乘问题
        System.out.println(method.task2(5));
    }

}
