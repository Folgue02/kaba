package me.folgue.kaba;

import me.folgue.kaba.interactive.Prompt;
import me.folgue.kaba.storage.StorageType;

public class App {
    private static boolean GRACEFUL_SHUTDOWN = false;
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!GRACEFUL_SHUTDOWN)
                System.out.println("\nClosing kaba like this might leave corrupted boards.\n");
            else
                System.out.println("Goodbye!");
        }));
        Prompt prompt = new Prompt(StorageType.Json);
        prompt.startInteractivePrompt();
        GRACEFUL_SHUTDOWN = true;
    }
}
