package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.CreationStrategy;
import eu.lucaventuri.fibry.Stereotypes;

public class MessageAndAnswer {
    public static void main(String[] args) {
        ActorSystem.setDefaultStrategy(BenchUtils.getStrategy(args));

        FibryBenchConfig.benchmark("Time to send and read synchronously 250K messages", () -> doBench());

    }

    // The blocking configurator is used to
    private static void doBench() throws Exception {
        //var num = new AtomicInteger();

        //var actorInfo = blockingConfigurator.schedule(() -> System.out.println(num.get()), 250);

        var actorAnswer = ActorSystem.anonymous().newActorWithReturn((Integer n) -> n * n);
        Stereotypes.def().runOnceSilent(() -> {
            for (int i = 0; i < 250_000; i++)
                actorAnswer.sendMessageReturn(i).get();
        }).closeOnExit(actorAnswer).waitForExit();
    }

}
