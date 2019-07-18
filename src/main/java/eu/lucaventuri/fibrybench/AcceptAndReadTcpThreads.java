package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.Exceptions;
import eu.lucaventuri.common.SystemUtils;
import eu.lucaventuri.fibry.SinkActorSingleMessage;
import eu.lucaventuri.fibry.Stereotypes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AcceptAndReadTcpThreads {
    public static void main(String[] args) throws Exception {
        int numRequests = 10_000;
        int numClients = 20;

        Stereotypes.setDebug(true);

        SystemUtils.printTimeEx(() -> acceptConnectionsAndRead(numRequests, numClients, Stereotypes.threads()), "Ms required to create " + numRequests + " connections: ");
    }

    static void acceptConnectionsAndRead(int numRequests, int numClients, Stereotypes.NamedStereotype configurator) throws Exception {
        var requestsToCreate = new AtomicInteger(numRequests);
        var numErrors = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(numRequests);
        var localHost = InetAddress.getLocalHost();

        try (SinkActorSingleMessage<Void> infoActor = configurator.schedule(() -> System.out.println("Latch: " + latch.getCount() + " - errors: " + numErrors.get()), 250);
             SinkActorSingleMessage<Void> acceptorActor = createAcceptor(configurator, latch)) {

            var clientActors = new SinkActorSingleMessage[numClients];
            for (int i = 0; i < numClients; i++)
                clientActors[i] = configurator.runOnce(createRunnableClientActor(requestsToCreate, numErrors, localHost));

            Exceptions.silence(latch::await);

            for (int i = 0; i < numClients; i++)
                clientActors[i].askExit();
        }
    }

    private static SinkActorSingleMessage<Void> createAcceptor(Stereotypes.NamedStereotype configurator, CountDownLatch latch) throws IOException {
        return configurator.tcpAcceptor(12345, conn -> {
            try (var is = conn.getInputStream(); var os = conn.getOutputStream()) {
                int length = is.read();
                var content = new String(is.readNBytes(length));
                os.write(content.toUpperCase().getBytes());
            } catch (IOException e) {
                System.err.println(e);
            }
            latch.countDown();
        }, null);
    }

    private static Runnable createRunnableClientActor(AtomicInteger requestsToCreate, AtomicInteger numErrors, InetAddress localHost) {
        return () -> {
            int n;

            while ((n = requestsToCreate.decrementAndGet()) >= 0) {
                try (var clientSocket = new Socket(localHost, 12345)) {
                    try (var os = clientSocket.getOutputStream(); var is = clientSocket.getInputStream()) {
                        String str = "Test" + n;

                        os.write(str.length());
                        os.write(str.getBytes());

                        String answer = new String(is.readAllBytes());

                        if (!answer.equals(str.toUpperCase()))
                            System.err.println("Wrong answer: " + str.toUpperCase() + " vs " + answer);
                    }
                } catch (IOException e) {
                    numErrors.incrementAndGet();
                }
            }
        };
    }
}

