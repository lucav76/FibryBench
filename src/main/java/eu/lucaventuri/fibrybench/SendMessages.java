package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.CreationStrategy;
import eu.lucaventuri.fibry.Stereotypes;

public class SendMessages {
    private static final int numMessages = 10_000_000;

    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        ActorSystem.setDefaultStrategy(BenchUtils.getStrategy(args));

        FibryBenchConfig.benchmark("Time to send  " + numMessages + " messagee to a " + (useFibers ? "fiber" : "thread"), () -> doBench());
    }

    private static void doBench() {
        var actor = ActorSystem.anonymous().newActor(message -> {});

        for(int i=0; i<numMessages; i++)
            actor.sendMessage("Test");

        actor.askExit();
    }
}
