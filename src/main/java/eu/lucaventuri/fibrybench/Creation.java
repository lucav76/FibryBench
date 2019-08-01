package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.SinkActor;
import eu.lucaventuri.fibry.SinkActorSingleMessage;

import java.util.List;
import java.util.Vector;

public class Creation {
    public static void main(String[] args) {
        boolean useFibers = BenchUtils.useFibers(args);
        var configurator = BenchUtils.getConfigurator(useFibers);
        var descr = useFibers ? "fibers" : "threads";
        int howMany = Integer.parseInt(args[1]);
        List<SinkActor> actors = new Vector<>();

        FibryBenchConfig.benchmark("Creating " + howMany + " " + descr, () -> {
            for (int i = 0; i < howMany; i++)
                actors.add(configurator.sink(null));
        }, () -> {
            System.out.println("Cleaning up " + actors.size() + " actors");
            actors.forEach(SinkActorSingleMessage::askExit);
            actors.forEach(SinkActorSingleMessage::waitForExit);
            actors.clear();
        });
    }
}
