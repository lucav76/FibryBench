package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.Stereotypes;

import java.util.concurrent.atomic.AtomicInteger;

public class HowManyFibers {
    public static void main(String[] args) {
        AtomicInteger created = new AtomicInteger();

        try {
            Stereotypes.fibers().schedule(() -> System.out.println("Fibers: " + created.get()), 250);

            while (true)
                Stereotypes.fibers().sink(created.incrementAndGet());
        }
        catch(Throwable e) {
            System.err.println(e);
            System.out.println("Total fibers: " + created.get());
        }
    }
}
