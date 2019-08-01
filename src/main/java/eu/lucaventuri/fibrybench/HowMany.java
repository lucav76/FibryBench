package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.Stereotypes;

import java.util.concurrent.atomic.AtomicInteger;

public class HowMany {
    public static void main(String[] args) {
        AtomicInteger created = new AtomicInteger();
        var configurator = BenchUtils.getConfigurator(args);

        try {
            configurator.schedule(() -> System.out.println("Threads: " + created.get()), 250);

            while (true)
                configurator.sink(created.incrementAndGet());
        }
        catch(Throwable e) {
            System.err.println(e);
            System.out.println("Total threads: " + created.get());
        }
    }
}
