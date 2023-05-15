package eu.hansolo.javafinder;

import eu.hansolo.jdktools.util.OutputFormat;

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

        // System information
        final SysInfo sysInfo = Finder.getSysInfo();

        // JDK distributions
        String searchPath;
        if (null == args || args.length == 0) {
            switch(finder.detectOperatingSystem()) {
                case WINDOWS -> searchPath = Finder.WINDOWS_JAVA_INSTALL_PATH;
                case LINUX   -> searchPath = Finder.LINUX_JAVA_INSTALL_PATH;
                case MACOS   -> searchPath = Finder.MACOS_JAVA_INSTALL_PATH;
                default      -> searchPath = File.separator;
            }
        } else {
            searchPath = args[0];
        }

        Set<DistributionInfo> distros = finder.getDistributions(List.of(searchPath));

        StringBuilder msgBuilder = new StringBuilder().append(CURLY_BRACKET_OPEN)
                                                      .append(QUOTES).append(FIELD_SEARCH_PATH).append(QUOTES).append(COLON).append(QUOTES).append(searchPath).append(QUOTES).append(COMMA)
                                                      .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                                                      .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.operatingSystem().getApiString()).append(QUOTES).append(COMMA)
                                                      .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getApiString()).append(QUOTES).append(COMMA)
                                                      .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getBitness().getApiString()).append(QUOTES)
                                                      .append(CURLY_BRACKET_CLOSE).append(COMMA)
                                                      .append(QUOTES).append(FIELD_DISTRIBUTIONS).append(QUOTES).append(COLON)
                                                      .append(distros.stream().map(distro -> distro.toString(OutputFormat.MINIMIZED)).collect(Collectors.joining(COMMA, SQUARE_BRACKET_OPEN, SQUARE_BRACKET_CLOSE)))
                                                      .append(CURLY_BRACKET_CLOSE);
        // Output
        System.out.println(msgBuilder);
        System.exit(0);
    }


    public static void main(String[] args) {
        new Main(args);
    }
}