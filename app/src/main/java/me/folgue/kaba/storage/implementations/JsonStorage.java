package me.folgue.kaba.storage.implementations;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import me.folgue.kaba.elements.Board;
import me.folgue.kaba.storage.IStorage;
import me.folgue.kaba.storage.StorageType;
import me.folgue.kaba.storage.exceptions.InvalidContent;
import me.folgue.kaba.storage.exceptions.ResourceNotAvailable;
import me.folgue.kaba.storage.exceptions.StorageException;

public class JsonStorage implements IStorage {
	private String filename;

	public JsonStorage(String filename) {
		this.filename = filename;
	}

    public StorageType getType() {
        return StorageType.Json;
    }

    @Override
    public void writeBoard(Board board) throws StorageException {
    	String fileContents = new Gson().toJson(board);

    	try (FileWriter fw = new FileWriter(this.filename)) {
    		fw.write(fileContents);
        } catch (IOException e) {
    		throw new ResourceNotAvailable(this.filename, e.getMessage());
    	}
    }


    @Override
    public Board getBoard() throws StorageException {
        Board parsedBoard;
    	try {
    		String fileContent = new String(Files.readAllBytes(Path.of(this.filename)));
    		parsedBoard = new Gson().fromJson(fileContent, Board.class);
    	} catch (IOException e) {
    		throw new ResourceNotAvailable(this.filename, e.getMessage());
    	} catch (JsonSyntaxException e) {
    		throw new InvalidContent(e.getMessage());
    	}

        if (parsedBoard == null)
            throw new InvalidContent("An attempt to load the board was made, but it was an empty json file.");

        return parsedBoard;
    }

    @Override
    public String getStorageName() {
        return String.format("JSON:%s", this.filename);
    }
}
