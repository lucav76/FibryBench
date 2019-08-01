package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.Stereotypes;

import java.io.IOException;

import static eu.lucaventuri.fibry.Stereotypes.HttpStringWorker;

public class HttpHelloWorld {
    public static void main(String[] args) throws IOException {
        var configurator = BenchUtils.getConfigurator(args);

        configurator.embeddedHttpServer(12345, exchange -> "Hello World!");
    }
}
