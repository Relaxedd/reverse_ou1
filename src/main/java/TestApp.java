public class TestApp {



    /**
     * TestApp without branching statements
     */
    public void test1() {
        int i = 0;
        int a = 6;
        System.out.println("a + i = " + a + i);

    }

    /**
     * TestApp with if-else branching statements
     */
    public void test2() {
        int number = 10;
        if (number > 0) {
            System.out.println("The number is positive.");
        } else if (number < 0) {
            System.out.println("The number is negative.");
        } else {
            System.out.println("The number is zero.");
        }
    }

    /**
     * TestApp with looping statement
     */
    public void test3() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Iteration: " + i);
        }
    }


}
