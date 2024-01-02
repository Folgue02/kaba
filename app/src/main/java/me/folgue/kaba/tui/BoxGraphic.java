package me.folgue.kaba.tui;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Comparator;

public class BoxGraphic {
    public char verticalSide = '║';
    public char horizontalSide = '═';
    public char topLeftCorner = '╔';
    public char topRightCorner = '╗';
    public char botLeftCorner = '╚';
    public char botRightCorner = '╝';
    public int maxParagraphLength = 100;
    public int leftPadding = 0;

    public BoxGraphic() {
    }

    public BoxGraphic(int leftPadding, int maxParagraphLength) {
        this.leftPadding = leftPadding;
        this.maxParagraphLength = maxParagraphLength;
    }

    public String formatBox(String message) {
        final int defaultWidth = 40;
        var baos = new ByteArrayOutputStream();
        var os = new PrintStream(baos);

        final int width = message.lines()
                .map(l -> l.length())
                .max(Comparator.naturalOrder())
                .orElse(defaultWidth);

        // Top bar
        os.println(repeatChar(' ', this.leftPadding) + this.topLeftCorner + repeatChar(this.horizontalSide, width + 2) + this.topRightCorner);

        // Body
        message.lines()
                .forEach(line -> {
                    os.println(
                            repeatChar(' ', this.leftPadding)
                            + this.verticalSide 
                            + ' '
                            + line 
                            + ' '
                            + repeatChar(' ', width - line.length()) + this.verticalSide
                    );
                });

        // Bottom bar
        os.println(repeatChar(' ', this.leftPadding) + this.botLeftCorner + repeatChar(this.horizontalSide, width + 2) + this.botRightCorner);
        
        return new String(baos.toByteArray());
    }

    public void printBox(String message) {
        System.out.println(this.formatBox(message));
    }

    /**
     * Repeats a character creating a string of the given length.
     * 
     * This method was made just to make the code slightly cleaner.
     * @param c Character to repeat
     * @param count Length of the new character.
     * @return New string with of the length given.
     */
    private static String repeatChar(char c, int count) {
        return (c + "").repeat(count);
    }
}
