import eu.lucaventuri.common.SystemUtils;
import eu.lucaventuri.fibrybench.BenchUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Experiments {
    private static final AtomicInteger num = new AtomicInteger();
    private static final int NUM_ITERATIONS = 200_000_000;

    static void doCalc(byte... a) {
        System.out.print("byte...");
    }
    /*static void doCalc(long a, long b) {
        System.out.print("long, long");
    }*/
    /*static void doCalc(Byte s1, Byte s2) {
        System.out.print("Byte, Byte");
    }*/
    public static void main (String[] args) {
        byte b = 5;
        doCalc(b, b);

        System.out.println("Void: " + SystemUtils.benchmarkEx(() -> doVoid(), 25));
        System.out.println("Int: " + SystemUtils.benchmarkEx(() -> doInt(), 25));
        System.out.println("Int: " + SystemUtils.benchmarkEx(() -> doInt(), 25));
        System.out.println("Void: " + SystemUtils.benchmarkEx(() -> doVoid(), 25));
    }

    private static void doVoid() {
        num.set(0);
        for(int i=0; i<NUM_ITERATIONS; i++)
            methodVoid();

        if(num.get()!=NUM_ITERATIONS)
            throw new IllegalStateException(num.getClass() + " vs" + NUM_ITERATIONS);
    }

    private static void doInt() {
        num.set(0);
        for(int i=0; i<NUM_ITERATIONS; i++)
            methodInt();

        if(num.get()!=NUM_ITERATIONS)
            throw new IllegalStateException(num.getClass() + " vs" + NUM_ITERATIONS);
    }

    private static void methodVoid() {
        num.incrementAndGet();
    }

    private static int methodInt() {
        return num.incrementAndGet();
    }
}
