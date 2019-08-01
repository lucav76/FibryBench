package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.SinkActorSingleMessage;
import eu.lucaventuri.fibry.Stereotypes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AcceptTcp {
    private static final int numRequests = 10_000;

    public static void main(String[] args) throws Exception {
        Stereotypes.setDebug(true);
        boolean useFibers = BenchUtils.useFibers(args);
        var configurator = BenchUtils.getConfigurator(useFibers);

        FibryBenchConfig.benchmark("Time required to create " + numRequests + " connections", () -> doBench(numRequests, configurator), 1);
    }

    static void doBench(int numRequests, Stereotypes.NamedStereotype configurator) throws Exception {
        var requestsToCreate = new AtomicInteger(numRequests);
        var numErrors = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(numRequests);

        try (SinkActorSingleMessage<Void> infoActor = configurator.schedule(() -> System.out.println("Latch: " + latch.getCount() + " - errors: " + numErrors.get()), 250);
             SinkActorSingleMessage<Void> acceptorActor = configurator.tcpAcceptor(12345, conn -> {
                 latch.countDown();
             }, null);
             var clientActor = configurator.sink(null)) {

            var localHost = InetAddress.getLocalHost();

            while (requestsToCreate.get() > 0)
                clientActor.execAsync(() -> {
                    try (var socket = new Socket(localHost, 12345)) {
                        requestsToCreate.decrementAndGet();
                    } catch (IOException e) {
                        numErrors.incrementAndGet();
                    }
                });

            latch.await();
        }
    }
}
