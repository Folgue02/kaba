package me.folgue.kaba.storage;

/**
 * Represents the type of storages.
 * @author folgue
 */
public enum StorageType {
    Json;

    public static StorageType ofString(String rawString) {
        if (rawString == null)
            return null;

        return switch (rawString.toLowerCase()) {
            case "json" -> Json;
            default -> null;
        };
    }
}
