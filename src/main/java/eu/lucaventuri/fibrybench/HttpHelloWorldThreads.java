package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.Stereotypes;

import java.io.IOException;

import static eu.lucaventuri.fibry.Stereotypes.HttpStringWorker;

public class HttpHelloWorldThreads {
    public static void main(String[] args) throws IOException {
        Stereotypes.threads().embeddedHttpServer(12345, new HttpStringWorker("/", e -> "Hello World!"));
    }
}
