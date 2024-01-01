package me.folgue.kaba.interactive;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;

class CommandStructureTest {

	@Test
	void parseValidCommand() {
		var input = "command arg1 arg2 \"still arg3\"";
        var result = new CommandStructure(input);        
        var expected = new CommandStructure("command", List.of("arg1", "arg2", "still arg3"));

        assertEquals(expected, result);
	}
    
    @Test
    void parseEmptyCommand() {
        var input = "";

        try {
            var cmdStruct = new CommandStructure(input);
            fail("Expected exception: " + cmdStruct);
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("Expected IllegalArgumentException instead of " + e);
        }
    }

    @Test
    void parseUnfinishedCommand() {
        var input = "command arg1 \"arg2 started";
        var expected = new CommandStructure("command", List.of("arg1", "arg2 started"));

        assertEquals(expected, new CommandStructure(input));
    }

    @Test
    void parseDoubleQuoteEnd() {
        var input = "command arg1 \"";
        var expected = new CommandStructure("command", List.of("arg1"));

        assertEquals(expected, new CommandStructure(input));
    }
}
