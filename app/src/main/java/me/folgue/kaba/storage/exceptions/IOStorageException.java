package me.folgue.kaba.storage.exceptions;

import java.io.IOException;

public class IOStorageException extends StorageException {
    public IOStorageException(IOException e) {
        super(String.format("Wrapped IOException: %s", e));
    }
}
