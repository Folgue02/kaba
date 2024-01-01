package me.folgue.kaba;

import java.util.Arrays;

import me.folgue.kaba.storage.StorageType;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StartupConfiguration {
    static class HelpArgument extends Exception {}

    public String address;
    public StorageType type;

    public StartupConfiguration(String[] args) throws ParseException, StartupConfiguration.HelpArgument {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option[] optionParameters = {
            new Option("a", "address", true, "Address to the storage"),
            new Option("t", "storage-type", true, "Type of storage to use."),
            new Option("h", "help", false, "Displays a help message.")
            /*Option.builder()
                .argName("a")
                .longOpt("address")
                .desc("Address to the storage.")
                .build(),
            Option.builder()
                .argName("t")
                .longOpt("storage-type")
                .desc("Type of storage to use.")
                .type(StorageType.class)
                .build(),
            Option.builder()
                .argName("h")
                .hasArg(false)
                .longOpt("help")
                .desc("Displays a help mesage.")
                .build()*/
        };

        Arrays.stream(optionParameters)
                .forEach(o -> options.addOption(o));

        CommandLine parsedArgs = parser.parse(options, args);

        if (parsedArgs.hasOption('h')) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("kaba", options);
            throw new StartupConfiguration.HelpArgument();
        }

        this.address = parsedArgs.getOptionValue('a');
        this.type = StorageType.ofString(parsedArgs.getOptionValue('t'));

        if (this.type == null) {
            throw new ParseException("Invalid storage type given.");
        }
    }
}