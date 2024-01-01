package me.folgue.kaba.storage.exceptions;

public class ResourceNotAvailable extends StorageException {
	public ResourceNotAvailable(String resourceName, String message) {
		super(String.format("Resource '%s' is not available: %s", resourceName, message));
	}
}
