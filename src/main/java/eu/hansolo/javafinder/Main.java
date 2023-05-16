package eu.hansolo.javafinder;

import eu.hansolo.jdktools.util.OutputFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.hansolo.javafinder.Constants.*;
import static eu.hansolo.jdktools.Constants.*;


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

        File file = new File(searchPath);
        if (!file.isDirectory()) {
            file = file.getParentFile();
        }
        if (null == file || !file.exists()) {
            System.out.println("Given path not found");
            System.exit(0);
        }
        file = null;

        Set<DistributionInfo> distros = finder.getDistributions(List.of(searchPath));

        StringBuilder msgBuilder;
        switch(outputType) {
            case CSV -> {
                msgBuilder = new StringBuilder().append("vendor,distribution,version,location,buildscope")
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
        System.out.println(msgBuilder);
        System.exit(0);
    }


    public static void main(String[] args) {
        new Main(args);
    }
}