package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.RunnableEx;
import eu.lucaventuri.common.SystemUtils;

public class FibryBenchConfig {
    public final static int numberRepetitions = 3;

    public static void  benchmark(String description, RunnableEx run) {
        benchmark(description, run, null, FibryBenchConfig.numberRepetitions);
    }

    public static void  benchmark(String description, RunnableEx run, RunnableEx cleanup) {
        benchmark(description, run, cleanup, FibryBenchConfig.numberRepetitions);
    }

    public static void benchmark(String description, RunnableEx run,  int numRequests) {
        benchmark(description, run, null, numRequests);
    }

    public static void benchmark(String description, RunnableEx run, RunnableEx cleanup, int numRequests) {
        var results = SystemUtils.benchmarkEx(run, cleanup, numRequests);

        System.out.println(description);
        System.out.println(results);
    }
}
