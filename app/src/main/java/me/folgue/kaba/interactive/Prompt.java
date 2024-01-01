package me.folgue.kaba.interactive;

import me.folgue.kaba.elements.TaskState;
import me.folgue.kaba.storage.IStorage;
import me.folgue.kaba.storage.StorageType;

import me.folgue.kaba.elements.Board;
import me.folgue.kaba.elements.Task;
import me.folgue.kaba.storage.StorageFactory;
import me.folgue.kaba.storage.exceptions.StorageException;
import me.folgue.kaba.utils.LineReaderSingleton;
import org.jline.reader.LineReader;

/**
 * Interactive utility to let the user interact with the storages and boards loaded
 * through stdin.
 * @author folgue
 */
public class Prompt {
    private Board board;
	private IStorage storage;
    private final StorageType storageType;

	public Prompt(IStorage storage) throws StorageException {
		this.storage = storage;
        this.board = storage.getBoard();
        this.storageType = storage.getType();
	}

    public Prompt(StorageType storageType) {
        this.storage = null;
        this.board = null;
        this.storageType = storageType;
    }

    public Prompt(StorageType storageType, String address) throws StorageException {
        this.storage = StorageFactory.getStorage(address, storageType);
        this.board = storage.getBoard();
        this.storageType = storageType;
    }

    /**
     * Checks if a board and a storage was loaded.
     * @return {@code true} if both of them were loaded, {@code false} if one 
     * of them wasn't loaded.
     */
    public boolean isLoaded() {
        return this.board != null && this.storage != null;
    }

    public void startInteractivePrompt() throws Exception {
        LineReader reader = LineReaderSingleton.getInstance();

        boolean inMenu = true;
        // Cry about it
        while (inMenu) {
            String storageName;
            if (this.isLoaded()) {
                storageName = this.storage.getStorageName();
            } else {
                storageName = "DISCONNECTED";
            }
            String prompt = String.format("(%s)%s>>>", this.storageType, storageName);
            CommandStructure cmdStruct;
            try {
                String userInput = reader.readLine(prompt);
                reader.getHistory().add(userInput);
                cmdStruct = new CommandStructure(userInput);
            } catch (IllegalArgumentException e) {
                // Empty command
                continue;
            }

            switch (cmdStruct.command) {
            case "load" ->  {
                if (this.isLoaded()) {
                    System.err.println("An storage is currently loaded (close with 'unload')");
                } else {
                    try {
                        this.load(cmdStruct.arguments.get(0));
                    } catch (StorageException e) {
                        e.printStackTrace(System.err);
                        System.err.println("The storage couldn't be loaded due to the previous error, it might not have been fully loaded.");
                    }
                }
            }
            case "unload" -> {
                if (this.isLoaded()) {
                    try {
                        this.unload();
                    } catch (StorageException e) {
                        System.err.println("An error occurred while unloading the storage (" + e.getMessage() + "), the storage might not have been unloaded properly.");
                    }
                } else {
                    System.err.println("No storage was loaded.");
                }
            }
            case "save", "write" -> {
                if (this.isLoaded()) {
                    try {
                        this.storage.writeBoard(this.board);
                        System.out.println("The board has been saved.");
                    } catch (StorageException e) {
                        System.err.println("There was an error while saving the board to the storage. The board might not have been saved or the storage might be corrupted.");
                    }
                } else {
                    System.err.println("No storage was loaded.");
                }
            }
            case "list" -> {
                if (!this.isLoaded()) {
                    System.err.println("No storage was loaded.");
                } else {
                    this.displayTasks();
                }
            }
            case "create" -> {
                if (cmdStruct.arguments.isEmpty()) {
                    System.err.println("You didn't specify the address of the new storage.");
                } else {
                    try {
                        System.out.printf("Creating a storage with address '%s' of type '%s'\n", cmdStruct.arguments.get(0), this.storageType);
                        StorageFactory.creationWizard(cmdStruct.arguments.get(0), this.storageType);
                    } catch (StorageException e) {
                        System.err.println("An error occurred while creating the storage with the specified address: " + e);
                    }
                }
            }
            case "newtask" -> {
                if (!this.isLoaded()) {
                    System.err.println("No storage was loaded.");
                } else {
                    if (cmdStruct.arguments.size() >= 2) {
                        this.createNewTask(cmdStruct.arguments.get(0), cmdStruct.arguments.get(1));
                    } else {
                        this.createNewTask();
                    }
                }
            }
            case "rmtask" -> {
                if (!this.isLoaded()) {
                    System.err.println("No storage was loaded.");
                } else {
                    if (cmdStruct.arguments.isEmpty())
                        System.err.println("You didn't specify the identifier of the task.");
                    else {
                        if (this.removeTask(cmdStruct.arguments.get(0))) {
                            System.out.println("Task removed.");
                        } else {
                            System.out.println("Task not removed (it either didn't exist, or the specified ID wasn't a number.");
                        }
                    }
                }
            }
            case "quit" -> {
                if (this.isLoaded())
                    System.out.println("There is a storage still loaded. Unload ('unload') it before quitting to avoid corruption.");
                else
                    inMenu = false;
            }
            }
        }
    }

    /**
     * Loads a storage (<i>of the same type as {@code this.storageType}</i>) with
     * the given address.
     * @param address Address of the resource that the storage will use.
     * @throws StorageException If something went wrong while getting the board 
     * from the storage.
     */
    public void load(String address) throws StorageException {
        this.storage = StorageFactory.getStorage(address, this.storageType);
        this.board = this.storage.getBoard();
    }

    /**
     * Sets the storage and board to {@code null}, before doing so, the method
     * {@link IStorage.close} method gets called.
     * @throws StorageException If something went wrong while closing the storage.
     */
    public void unload() throws StorageException {
        if (this.storage != null) {
            this.storage.close(this.board);
        }
        this.storage = null;
        this.board = null;
    }

    /**
     * Displays all the tasks. (<i>this method assumes that the board is not null
     * </i>).
     */
    public void displayTasks() {
        if (this.board.tasks.isEmpty()) {
            System.out.println("The board is empty.");
        }

        for (Task task : this.board.tasks) {
            System.out.println();
            System.out.println(task);
        }
    }

    /**
     * Starts an interactive wizard to create a new task. (<i>this method 
     * assumes that the {@code this.board} is not {@code null}</i>).
     */
    private void createNewTask() {
        LineReader reader = LineReaderSingleton.getInstance();

        String name = reader.readLine("What's the name of the task? ");

        String description = reader.readLine("What's the description of the task? ");

        this.createNewTask(name, description);
    }

    /**
     * Creates a new task, without any interactive wizard in the way.
     * @param name Name of the new task.
     * @param description Description of the new task.
     */
    private void createNewTask(String name, String description) {
        this.board.createNewTask(name, description, TaskState.Todo);
    }

    /**
     * Removes a task with the given identifier. This method is also in charge of
     * parsing the {@code identifier} string into an integer. If either the task
     * doesn't exist or the given identifier can't be parsed into an integer, 
     * {@code false} gets returned, otherwise {@code true} will be returned.
     * @param identifier Identifier of the task (<i>has to be an integer</i>).
     * @return {@code true} if the task was removed, or {@code false} if the task
     * didn't exist or the identifier couldn't be parsed into an integer.
     */
    private boolean removeTask(String identifier) {
        int targetId;
        try {
            targetId = Integer.parseInt(identifier);
        } catch (Exception e) {
            return false;
        }

        return this.board.removeTask(targetId);
    }
}
