package me.folgue.kaba.interactive;

import java.util.*;

public class CommandStructure {
	public String command;
	public List<String> arguments;

    public CommandStructure(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }
	
	public CommandStructure(String cmdLine) {
		var segments = parseCmdLine(cmdLine);
		
		if (segments.isEmpty()) {
			throw new IllegalArgumentException("Empty command line.");
		} else if (segments.size() == 1) {
			this.command = segments.get(0);
			this.arguments = new ArrayList<>();
		} else {
			this.command = segments.get(0);
			this.arguments = new ArrayList<>(segments.subList(1, segments.size()));
		}
	}
	
	private static List<String> parseCmdLine(String cmdLine) {
		var parsed = new ArrayList<String>();
		boolean quoteStatus = false;
		StringBuilder buffer = new StringBuilder(); 

		for (int i=0;i < cmdLine.length();i++) {
			char currChar = cmdLine.charAt(i);
			
			if (currChar == ' ' && !quoteStatus) {
				parsed.add(buffer.toString());
				buffer = new StringBuilder();
			} else if (currChar == '\"') {
				quoteStatus = !quoteStatus;
			} else {
				buffer.append(currChar);
			}

            if (i == cmdLine.length() - 1 && !buffer.isEmpty()) {
                parsed.add(buffer.toString());
            }
		}
		
		return parsed;
	}
	
	@Override
	public boolean equals(Object other) {
        if (other == this)
            return true;

		if (other instanceof CommandStructure cmdStruct) {
		    return cmdStruct.command.equals(this.command) 
		        && cmdStruct.arguments.equals(this.arguments);
		} else {
			return false;
		}
	}

    @Override
    public String toString() {
        return String.format("Command: %s, Arguments: %s", this.command, this.arguments);
    }
}
