package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.SystemUtils;
import eu.lucaventuri.fibry.*;

public class CreationThreads {
    public static void main(String[] args) {
        SystemUtils.printTime(() -> {
            for(int i=0; i<10000; i++)
                Stereotypes.threads().sink(null);
        }, "Creating 10K threads");
    }
}
