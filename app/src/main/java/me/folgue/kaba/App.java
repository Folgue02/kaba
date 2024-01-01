package me.folgue.kaba;

import me.folgue.kaba.interactive.Prompt;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

public class App {
    public static final String VERSION = "0.0.2";
    private static boolean GRACEFUL_SHUTDOWN = false;
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!GRACEFUL_SHUTDOWN)
                System.out.println("\nClosing kaba like this might leave corrupted boards.\n");
        }));

        StartupConfiguration startConfig;
        try {
            startConfig = new StartupConfiguration(args);
        } catch (ParseException e) {
            GRACEFUL_SHUTDOWN = true;
            System.err.println("An error has occurred while parsing the cli's arguments: " + e.getMessage());
            System.exit(1);
            return;
        } catch (StartupConfiguration.HelpArgument e) {
            GRACEFUL_SHUTDOWN = true;
            System.exit(0);
            return;
        } catch (StartupConfiguration.VersionArgument e) {
            GRACEFUL_SHUTDOWN = true;
            System.out.printf("Kaba version v%s\n", App.VERSION);
            System.out.printf("Running on %s version %s\n", System.getProperty("os.name"), System.getProperty("os.version"));
            System.out.printf("JVM version %s\n", System.getProperty("java.runtime.version"));
            System.exit(0);
            return;
        }

        Prompt prompt;
        if (startConfig.address != null) {
            prompt = new Prompt(startConfig.type, startConfig.address);
        } else {
            prompt = new Prompt(startConfig.type);
        }

        prompt.startInteractivePrompt();
        GRACEFUL_SHUTDOWN = true;
    }
}