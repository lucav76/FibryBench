package eu.lucaventuri.fibrybench;

import eu.lucaventuri.common.Exitable;
import eu.lucaventuri.fibry.CreationStrategy;
import eu.lucaventuri.fibry.Stereotypes;

public final class BenchUtils {
    private BenchUtils() { /** Utility class */}

    public static Stereotypes.NamedStereotype getConfigurator(String args[]) {
        return getConfigurator(useFibers(args));
    }

    public static Stereotypes.NamedStereotype getConfigurator(boolean useFibers) {
        return useFibers ? Stereotypes.fibers() : Stereotypes.threads();
    }

    public static Stereotypes.NamedStereotype getConfigurator(CreationStrategy strategy) {
        return strategy==CreationStrategy.FIBER ? Stereotypes.fibers() : (strategy==CreationStrategy.THREAD? Stereotypes.threads():Stereotypes.auto());
    }

    public static Stereotypes.NamedStereotype getConfigurator(CreationStrategy strategy, Exitable.CloseStrategy closeStrategy) {
        return strategy==CreationStrategy.FIBER ? Stereotypes.fibers(closeStrategy) : (strategy==CreationStrategy.THREAD? Stereotypes.threads(closeStrategy):Stereotypes.auto(closeStrategy));
    }

    public static CreationStrategy getStrategy(boolean useFibers) {
        return useFibers ? CreationStrategy.FIBER : CreationStrategy.THREAD;
    }

    public static CreationStrategy getStrategy(String args[]) {
        return useFibers(args) ? CreationStrategy.FIBER : CreationStrategy.THREAD;
    }

    public static boolean useFibers(String[] args) {
        if (args.length < 1) {
            System.err.println("Please specify F to use fibers or T to use Threads ");
            System.exit(1);
        }

        if ("F".equalsIgnoreCase(args[0])) {
            System.out.println("Using fibers");

            return true;
        }

        if ("T".equalsIgnoreCase(args[0])) {
            System.out.println("Using threads");

            return false;
        }

        System.out.println("Please specify F to use fibers or T to use Threads. " + args[0] + " is not a valid argument");

        System.exit(1);
        return false;
    }

}
