package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MessageAndMessage {
    private static int numMessages = 250_000;

    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        var strategy = BenchUtils.getStrategy(useFibers);
        var configurator = BenchUtils.getConfigurator(useFibers);

        FibryBenchConfig.benchmark("Time to send and receive " + numMessages + " messagee", () -> doBench(strategy, configurator));
    }

    private static void doBench(CreationStrategy strategy, Stereotypes.NamedStereotype configurator) {

        AtomicInteger lastNum = new AtomicInteger();

        AtomicReference<Actor<Integer, Void, Void>> refActorQuestion = new AtomicReference<>();

        long start = System.currentTimeMillis();

        var actorAnswer = ActorSystem.anonymous().strategy(strategy).newActor((Integer n) -> refActorQuestion.get().sendMessage(n + 1));
        var actorInfo = configurator.schedule(() -> System.out.println(lastNum.get()), 250);
        refActorQuestion.set(ActorSystem.anonymous().strategy(strategy).newActor((Integer n, PartialActor<Integer, Void> thisActor) -> {
            if (n == numMessages) {
                System.out.println("Time to send and receive " + numMessages + " messages: " + (System.currentTimeMillis() - start) + "ms");
                thisActor.askExit();
            } else {
                actorAnswer.sendMessage(n);
                lastNum.incrementAndGet();
            }
        }).sendMessage(0).closeOnExit(actorAnswer, actorInfo));

        refActorQuestion.get().waitForExit();
    }

}
