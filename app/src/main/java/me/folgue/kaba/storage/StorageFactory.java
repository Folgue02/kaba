
package me.folgue.kaba.storage;

import com.google.gson.Gson;
import me.folgue.kaba.elements.Board;
import me.folgue.kaba.storage.exceptions.StorageException;
import me.folgue.kaba.storage.implementations.JsonStorage;
import me.folgue.kaba.storage.exceptions.IOStorageException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import me.folgue.kaba.utils.LineReaderSingleton;
import org.jline.reader.LineReader;

/**
 * 
 * @author folgue
 */
public abstract class StorageFactory {

    /**
     * Creates a storage that uses the given address of the type specified.
     * @param address Address that will be used by the storage.
     * @param type Type of the storage.
     * @return Storage
     */
    public static IStorage getStorage(String address, StorageType type) {
        return switch (type) {
            case Json -> new JsonStorage(address);
        };
    }

    /**
     * Starts an interactive wizard for creating a storage.
     * @param address Address used by the future storage.
     * @param type 
     * @throws StorageException 
     */
    public static void creationWizard(String address, StorageType type) throws StorageException {
        switch (type) {
            case Json -> {
                jsonCreationWizard(address);
            }
        }
    }

    /**
     * Starts an interactive wizard for creating a {@link JsonStorage} 
     * @param address
     * @throws IOStorageException 
     */
    private static void jsonCreationWizard(String address) throws IOStorageException {
        LineReader sc = LineReaderSingleton.getInstance();

        String identifier = sc.readLine("What will be the ID string of the new board? ");
        IStorage jsonStorage = getStorage(address, StorageType.Json);

        Board board = new Board(identifier);
        String jsonContent = new Gson().toJson(board);

        try(FileWriter fw = new FileWriter(jsonStorage.getAddress())) {
            fw.write(jsonContent);
        } catch (IOException e) {
            throw new IOStorageException(e);
        }
    }
}