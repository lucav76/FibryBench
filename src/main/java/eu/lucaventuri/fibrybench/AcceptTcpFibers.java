package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.SystemUtils;
import eu.lucaventuri.fibry.Stereotypes;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

public class AcceptTcpFibers {
    public static void main(String[] args) throws Exception {
        int numRequests = 10_000;

        SystemUtils.printTimeEx( () -> AcceptTcpThreads.acceptConnections(numRequests, Stereotypes.fibers()), "Ms required to create " + numRequests + " connections: ");
        System.exit(0);
    }
}
