package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.Stereotypes;

import java.io.IOException;

import static eu.lucaventuri.fibry.Stereotypes.HttpStringWorker;

public class HttpHelloWorldFibers {
    public static void main(String[] args) throws IOException {
        Stereotypes.fibers().embeddedHttpServer(12345, new HttpStringWorker("/", e -> "Hello World!"));
    }
}
