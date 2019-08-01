package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.CreationStrategy;
import eu.lucaventuri.fibry.SinkActorSingleMessage;
import eu.lucaventuri.fibry.Stereotypes;

public class SendMessages4Threads {
    private static final int numMessages = 10_000_000;

    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        var strategy = BenchUtils.getStrategy(useFibers);
        Stereotypes.NamedStereotype configurator = BenchUtils.getConfigurator(useFibers);

        FibryBenchConfig.benchmark("Time for 4 threads to send  " + (numMessages * 4) + " messages to a " + (useFibers ? "fiber" : "thread"), () -> doBench(strategy, configurator));
    }

    private static void doBench(CreationStrategy strategy, Stereotypes.NamedStereotype configurator) {
        var actor1 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });
        var actor2 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });
        var actor3 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });
        var actor4 = ActorSystem.anonymous().strategy(strategy).newActor(message -> {
        });

        SinkActorSingleMessage<Void> client1 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor1.sendMessage("Test1");
        });

        SinkActorSingleMessage<Void> client2 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor2.sendMessage("Test2");
        });

        SinkActorSingleMessage<Void> client3 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor3.sendMessage("Test3");
        });

        SinkActorSingleMessage<Void> client4 = configurator.runOnce(() -> {
            for (int i = 0; i < numMessages; i++)
                actor4.sendMessage("Test4");
        });

        client1.waitForExit();
        client2.waitForExit();
        client3.waitForExit();
        client4.waitForExit();
        actor1.askExit();
        actor2.askExit();
        actor3.askExit();
        actor4.askExit();
    }
}
