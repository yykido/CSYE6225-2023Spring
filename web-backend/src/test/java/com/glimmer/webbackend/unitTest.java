package com.glimmer.webbackend;

import org.junit.jupiter.api.Test;

import static com.glimmer.webbackend.utils.TimeTool.getNowTime;

/**
 * @ClassNAME unitTest
 * @Description TODO
 * @Author glimmer
 * @Date 2023/4/14 23:04
 * @Version 1.0
 */
public class unitTest {
    @Test
    public void testPrint() {
        System.out.println("Hello World!");
    }

    @Test
    public void testGetNowTime() {
      String now = getNowTime();
      System.out.println(now);
    }

}