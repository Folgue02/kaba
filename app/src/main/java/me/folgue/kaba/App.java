package me.folgue.kaba;

import me.folgue.kaba.interactive.Prompt;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

public class App {
    private static boolean GRACEFUL_SHUTDOWN = false;
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!GRACEFUL_SHUTDOWN)
                System.out.println("\nClosing kaba like this might leave corrupted boards.\n");
            else
                System.out.println("Goodbye!");
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
