package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.CreationStrategy;

import java.util.concurrent.atomic.AtomicInteger;

public class SendMessagesNamed {
    private static final int numMessages = 10_000_000;
    public static final AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        var strategy = BenchUtils.getStrategy(useFibers);

        FibryBenchConfig.benchmark("Time to send  " + numMessages + " messagee to a " + (useFibers ? "fiber" : "thread"), () -> doBench(strategy));
    }

    private static void doBench(CreationStrategy strategy) {
        String name = "test" + num.incrementAndGet();
        ActorSystem.named(name).strategy(strategy).newActor(message -> {});

        for(int i=0; i<numMessages; i++)
            ActorSystem.sendMessage(name, "Test", false);
    }
}
