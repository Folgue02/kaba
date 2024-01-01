package me.folgue.kaba.utils;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public abstract class LineReaderSingleton {
    private static LineReader instance;


    public static LineReader getInstance() {
        if (instance == null) {
            Terminal terminal;
            try {
                terminal = TerminalBuilder.terminal();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            LineReader reader = LineReaderBuilder
                    .builder()
                    .terminal(terminal)
                    .build();
            instance = reader;
        }
        return instance;
    }
}
