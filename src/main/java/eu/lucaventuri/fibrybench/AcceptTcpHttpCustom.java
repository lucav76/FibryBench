package eu.lucaventuri.fibrybench;

import eu.lucaventuri.fibry.ActorSystem;
import eu.lucaventuri.fibry.Stereotypes;

import java.io.InputStream;
import java.io.OutputStream;

public class AcceptTcpHttpCustom {
    public static void main(String[] args) throws Exception {
        ActorSystem.setDefaultStrategy(BenchUtils.getStrategy(args));

        Stereotypes.def().tcpAcceptorSilent(12345, conn -> {
            try (var is = conn.getInputStream(); var os = conn.getOutputStream()) {
                while (is.read() != '\n' || is.read() != '\r' || is.read() != '\n') { }
                os.write("HTTP/1.1 200 OK\r\nContent-Length: 6\r\n\r\nHello!".getBytes());
            }
        }, null).waitForExit();
    }


}
