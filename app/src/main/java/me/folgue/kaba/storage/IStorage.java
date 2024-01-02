package me.folgue.kaba.storage;

import java.util.*;

import me.folgue.kaba.elements.Board;
import me.folgue.kaba.storage.exceptions.StorageException;

/**
 * Represents an storage unit.
 * @author folgue
 */
public interface IStorage { 
    /**
     * @return Name of the storage to be displayed.
     */
    String getStorageName();

    /**
     * Fetches the board from the storage.
     * @return The fetched board.
     * @throws StorageException If something went wrong while fetching the board
     * from the storage.
     */
    Board getBoard() throws StorageException;

    /**
     * Writes the board into the storage.
     * @param board Board to be saved.
     * @throws StorageException If something went wrong during the write operation.
     */
    void writeBoard(Board board) throws StorageException;

    /**
     * @return The type of storage of the object.
     */
    StorageType getType();

    /**
     * Returns the address used by the storage.
     * @return Address used by the storage.
     */
    String getAddress();

    /**
     * Method to be called before removing the storage. A {@link Board} is passed
     * with the intention to be saved before closing.
     * @param board The modified board.
     * @throws StorageException If something went wrong during the close operation.
     */
    default void close(Board board) throws StorageException {
        this.writeBoard(board);
        this.close();
    }

    /**
     * Method to be called before removing the storage. No board is passed since
     * it's not intended to be saved.
     */
    default void close() {
    }
}
