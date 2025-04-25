package org.ses.re.assignment1.testapp;

public class TestApp {

    int staticVar = 0;

    /**
     * TestApp without branching statements
     */
    public void test1() {
        int i = 0;
        int a = 6;
        int b = 7;
        a = 5;
        staticVar = 1;
    }

    /**
     * TestApp with if-else branching statements
     */
    public void test2() {
        int number = 10;
        int a;
        int b;
        int c;
        if (number > 0) {
            a = number;
            staticVar = 2;
        } else if (number < 0) {
            b = number;
            return;
        } else {
            c = number;
        }
    }

    /**
     * TestApp with looping statement
     */
    public void test3() {
        int number = 0;
        for (int i = 1; i <= 5; i++) {
            number += i;
        }
    }


}
