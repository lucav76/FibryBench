package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.CreationStrategy;
import eu.lucaventuri.fibry.SinkActorSingleMessage;
import eu.lucaventuri.fibry.Stereotypes;

public class SendMessages2Threads {
    private static final int numMessages = 10_000_000;

    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        var strategy = BenchUtils.getStrategy(useFibers);
        Stereotypes.NamedStereotype configurator = BenchUtils.getConfigurator(useFibers);

        FibryBenchConfig.benchmark("Time for 2 threads to send  " + (numMessages * 2) + " messages to a " + (useFibers ? "fiber" : "thread"), () -> doBench(strategy, configurator));
    }

    private static void doBench(CreationStrategy strategy, Stereotypes.NamedStereotype configurator) {
        var actor1 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });
        var actor2 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });

        SinkActorSingleMessage<Void> client1 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor1.sendMessage("Test1");
        });

        SinkActorSingleMessage<Void> client2 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor2.sendMessage("Test2");
        });

        client1.waitForExit();
        client2.waitForExit();
        actor1.askExit();
        actor2.askExit();
    }
}
