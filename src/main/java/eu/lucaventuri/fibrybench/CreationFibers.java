package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.SystemUtils;
import eu.lucaventuri.fibry.Stereotypes;

public class CreationFibers {
    public static void main(String[] args) {
        SystemUtils.printTime(() -> {
            for(int i=0; i<1_000_000; i++)
                Stereotypes.fibers().sink(null);
        }, "Creating 1M fibers");
    }
}
