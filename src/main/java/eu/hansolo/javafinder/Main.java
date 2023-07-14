/*
 * Copyright (c) 2023 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.javafinder;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.hansolo.javafinder.Constants.FIELD_ARCHITECTURE;
import static eu.hansolo.javafinder.Constants.FIELD_BIT;
import static eu.hansolo.javafinder.Constants.FIELD_DISTRIBUTIONS;
import static eu.hansolo.javafinder.Constants.FIELD_OPERATING_SYSTEM;
import static eu.hansolo.javafinder.Constants.FIELD_SEARCH_PATH;
import static eu.hansolo.javafinder.Constants.FIELD_SYSINFO;
import static eu.hansolo.javafinder.Constants.FIELD_TIMESTAMP;
import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENT;
import static eu.hansolo.jdktools.Constants.INDENTED_QUOTES;


public class Main {
    private static final String VERSION = "17.0.27";
    private        final Finder finder;


    // ******************** Constructors **************************************
    public Main() {
        this(null);
    }
    public Main(final String[] args) {
        this.finder = new Finder();
        findJava(args);
    }


    // ******************** Methods *******************************************
    private void findJava(final String[] args) {
        final long timestamp = Instant.now().getEpochSecond();

        // System information
        final SysInfo sysInfo = Finder.getSysInfo();

        // JDK distributions
        String     searchPath = "";
        OutputType outputType = OutputType.JSON;
        if (null == args || args.length == 0) {
            searchPath = Finder.getDefaultSearchPath();
        } else {
            if (args.length == 1) {
                final String firstArgument = args[0];
                if (firstArgument.equals("-h") || firstArgument.equals("-H")) {
                    System.out.println("""
                                       Usage:
                                       javafinder
                                       -> Will in default search paths:
                                          Windows: C:\\Program Files\\Java\\
                                          Linux  : /usr/lib/jvm
                                          MacOS  : /System/Volumes/Data/Library/Java/JavaVirtualMachines/
                                       
                                       javafinder -h
                                       -> Shows this info
                                       
                                       javafinder -v
                                       -> Shows the version
                                       
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
                } else if (firstArgument.equals("-v") || firstArgument.equals("-V")) {
                    System.out.println("JavaFinder " + Constants.BRIGHT_BLUE + VERSION + Constants.RESET_COLOR);
                    System.exit(0);
                } else if (firstArgument.equals("csv")) {
                    outputType = OutputType.CSV;
                    searchPath = Finder.getDefaultSearchPath();
                } else if (firstArgument.equals("json")) {
                    outputType = OutputType.BEAUTIFIED_JSON;
                    searchPath = Finder.getDefaultSearchPath();
                } else {
                    searchPath = firstArgument;
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
            System.out.println(Constants.RED + "Given path not found" + Constants.RESET_COLOR);
            System.exit(1);
        }

        Set<DistributionInfo> distros = finder.getDistributions(List.of(searchPath));

        StringBuilder msgBuilder;
        switch(outputType) {
            case CSV ->
                msgBuilder = new StringBuilder().append("Vendor,Distribution,Version,Timestamp,Path,Type,InUse,Timestamp")
                                                .append(NEW_LINE)
                                                .append(distros.stream().map(distro -> distro.toString(OutputType.CSV)).collect(Collectors.joining()));
            case BEAUTIFIED_JSON ->
                msgBuilder = new StringBuilder().append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                                                .append(INDENTED_QUOTES).append(FIELD_TIMESTAMP).append(QUOTES).append(COLON).append(timestamp).append(COMMA_NEW_LINE)
                                                .append(INDENTED_QUOTES).append(FIELD_SEARCH_PATH).append(QUOTES).append(COLON).append(QUOTES).append(searchPath.replaceAll("\\\\", "\\\\\\\\")).append(QUOTES).append(COMMA_NEW_LINE)
                                                .append(INDENTED_QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                                                .append(INDENT).append(INDENTED_QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.operatingSystem().getUiString()).append(QUOTES).append(COMMA_NEW_LINE)
                                                .append(INDENT).append(INDENTED_QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getUiString()).append(QUOTES).append(COMMA_NEW_LINE)
                                                .append(INDENT).append(INDENTED_QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getBitness().getUiString()).append(QUOTES).append(NEW_LINE)
                                                .append(INDENT).append(CURLY_BRACKET_CLOSE).append(COMMA_NEW_LINE)
                                                .append(INDENTED_QUOTES).append(FIELD_DISTRIBUTIONS).append(QUOTES).append(COLON).append(SQUARE_BRACKET_OPEN).append(NEW_LINE)
                                                .append(distros.stream().map(distro -> distro.toString(OutputType.BEAUTIFIED_JSON)).collect(Collectors.joining(COMMA_NEW_LINE)))
                                                .append(NEW_LINE).append(INDENT).append(SQUARE_BRACKET_CLOSE).append(NEW_LINE)
                                                .append(CURLY_BRACKET_CLOSE);
            default ->
                msgBuilder = new StringBuilder().append(CURLY_BRACKET_OPEN)
                                                .append(QUOTES).append(FIELD_TIMESTAMP).append(QUOTES).append(COLON).append(timestamp).append(COMMA)
                                                .append(QUOTES).append(FIELD_SEARCH_PATH).append(QUOTES).append(COLON).append(QUOTES).append(searchPath.replaceAll("\\\\", "\\\\\\\\")).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                                                .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.operatingSystem().getUiString()).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getUiString()).append(QUOTES).append(COMMA)
                                                .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(sysInfo.architecture().getBitness().getUiString()).append(QUOTES)
                                                .append(CURLY_BRACKET_CLOSE).append(COMMA)
                                                .append(QUOTES).append(FIELD_DISTRIBUTIONS).append(QUOTES).append(COLON)
                                                .append(distros.stream().map(distro -> distro.toString(OutputType.JSON)).collect(Collectors.joining(COMMA, SQUARE_BRACKET_OPEN, SQUARE_BRACKET_CLOSE)))
                                                .append(CURLY_BRACKET_CLOSE);
        }

        // Output
        if (!distros.isEmpty()) { System.out.println(msgBuilder); }
        System.exit(0);
    }

    public static void main(String[] args) {
        new Main(args);
    }
}