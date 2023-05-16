package eu.hansolo.javafinder;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.hansolo.javafinder.Constants.FIELD_ARCHITECTURE;
import static eu.hansolo.javafinder.Constants.FIELD_BIT;
import static eu.hansolo.javafinder.Constants.FIELD_DISTRIBUTIONS;
import static eu.hansolo.javafinder.Constants.FIELD_OPERATING_SYSTEM;
import static eu.hansolo.javafinder.Constants.FIELD_SEARCH_PATH;
import static eu.hansolo.javafinder.Constants.FIELD_SYSINFO;
import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_OPEN;


public class Main {
    private final Finder finder;


    public Main() {
        this(null);
    }
    public Main(final String[] args) {
        this.finder = new Finder();
        findJava(args);
    }

    private void findJava(final String[] args) {
        // System information
        final SysInfo sysInfo = Finder.getSysInfo();

        // JDK distributions
        String     searchPath = "";
        OutputType outputType = OutputType.JSON;
        if (null == args || args.length == 0) {
            switch(finder.detectOperatingSystem()) {
                case WINDOWS -> searchPath = Finder.WINDOWS_JAVA_INSTALL_PATH;
                case LINUX   -> searchPath = Finder.LINUX_JAVA_INSTALL_PATH;
                case MACOS   -> searchPath = Finder.MACOS_JAVA_INSTALL_PATH;
                default      -> searchPath = "";
            }
        } else {
            if (args.length == 1) {
                if (args[0].equals("-h") || args[0].equals("-H")) {
                    System.out.println("""
                                       Usage:
                                       javafinder OUTPUTFORMAT PATH
                                       
                                       OUTPUTFORMAT: csv, json
                                       If not present it will default to json
                                       
                                       PATH: A valid path on your local filesystem
                                       
                                       Examples:
                                       javafinder csv c:\\
                                       
                                       javafinder json c:\\
                                       
                                       javafinder c:\\
                                       
                                       javafinder /System/Volumes/Data/Library/Java/JavaVirtualMachines
                                       """);
                    System.exit(0);
                } else {
                    searchPath = args[0];
                }
            } else {
                outputType = OutputType.fromText(args[0]);
                if (OutputType.NOT_FOUND == outputType) {
                    outputType = OutputType.JSON;
                }
                searchPath = args[1];
            }
        }

        // Check for valid searchPath
        final File f = new File(searchPath);
        if (!f.exists() || !f.isDirectory()) {
            System.out.println("Given path not found");
            System.exit(1);
        }

        Set<DistributionInfo> distros = finder.getDistributions(List.of(searchPath));

        StringBuilder msgBuilder;
        switch(outputType) {
            case CSV -> {
                msgBuilder = new StringBuilder().append("Vendor,Distribution,Version,Location,Type")
                                                .append(NEW_LINE)
                                                .append(distros.stream().map(distro -> distro.toString(OutputType.CSV)).collect(Collectors.joining()));
            }
            default  -> {
                msgBuilder = new StringBuilder().append(CURLY_BRACKET_OPEN)
                                                .append(QUOTES).append(FIELD_SEARCH_PATH).append(QUOTES).append(COLON).append(QUOTES).append(searchPath).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                                                .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.operatingSystem().getUiString()).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getUiString()).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getBitness().getUiString()).append(QUOTES)
                                                .append(CURLY_BRACKET_CLOSE).append(COMMA)
                                                .append(QUOTES).append(FIELD_DISTRIBUTIONS).append(QUOTES).append(COLON)
                                                .append(distros.stream().map(distro -> distro.toString(OutputType.JSON)).collect(Collectors.joining(COMMA, SQUARE_BRACKET_OPEN, SQUARE_BRACKET_CLOSE)))
                                                .append(CURLY_BRACKET_CLOSE);
            }
        }

        // Output
        if (!distros.isEmpty()) { System.out.println(msgBuilder); }
        System.exit(0);
    }

    public static void main(String[] args) {
        new Main(args);
    }
}