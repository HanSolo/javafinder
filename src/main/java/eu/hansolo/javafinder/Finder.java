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

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.OperatingMode;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.scopes.BuildScope;
import eu.hansolo.jdktools.util.OutputFormat;
import eu.hansolo.jdktools.versioning.Semver;
import eu.hansolo.jdktools.versioning.VersionNumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Finder {
    public static final  String            MACOS_JAVA_INSTALL_PATH   = "/System/Volumes/Data/Library/Java/JavaVirtualMachines/";
    public static final  String            WINDOWS_JAVA_INSTALL_PATH = "C:\\Program Files\\Java\\";
    public static final  String            LINUX_JAVA_INSTALL_PATH   = "/usr/lib/jvm";
    private static final Pattern           GRAALVM_VERSION_PATTERN   = Pattern.compile("(.*graalvm\\s)(.*)(\\s\\(.*)");
    private static final Matcher           GRAALVM_VERSION_MATCHER   = GRAALVM_VERSION_PATTERN.matcher("");
    private static final Pattern           ZULU_BUILD_PATTERN        = Pattern.compile("\\((build\\s)(.*)\\)");
    private static final Matcher           ZULU_BUILD_MATCHER        = ZULU_BUILD_PATTERN.matcher("");
    private static final String[]          MAC_JAVA_HOME_CMDS        = { "/bin/sh", "-c", "echo $JAVA_HOME" };
    private static final String[]          LINUX_JAVA_HOME_CMDS      = { "/usr/bin/sh", "-c", "echo $JAVA_HOME" };
    private static final String[]          WIN_JAVA_HOME_CMDS        = { "cmd.exe", "/c", "echo %JAVA_HOME%" };
    private static final String[]          DETECT_ALPINE_CMDS        = { "/bin/sh", "-c", "cat /etc/os-release | grep 'NAME=' | grep -ic 'Alpine'" };
    private static final String[]          UX_DETECT_ARCH_CMDS       = { "/bin/sh", "-c", "uname -m" };
    private static final String[]          MAC_DETECT_ROSETTA2_CMDS  = { "/bin/sh", "-c", "sysctl -in sysctl.proc_translated" };
    private static final String[]          WIN_DETECT_ARCH_CMDS      = { "cmd.exe", "/c", "SET Processor" };
    private static final Pattern           ARCHITECTURE_PATTERN      = Pattern.compile("(PROCESSOR_ARCHITECTURE)=([a-zA-Z0-9_\\-]+)");
    private static final Matcher           ARCHITECTURE_MATCHER      = ARCHITECTURE_PATTERN.matcher("");
    private final        List<ProcessInfo> usedDistros;
    private              ExecutorService   service                   = Executors.newSingleThreadExecutor();
    private              Properties        releaseProperties         = new Properties();
    private              OperatingSystem   operatingSystem           = detectOperatingSystem();
    private              Architecture      architecture              = detectArchitecture();
    private              String            javaFile                  = OperatingSystem.WINDOWS == operatingSystem ? "java.exe" : "java";
    private              String            javaHome                  = "";
    private              boolean           isAlpine                  = false;


    // ******************** Constructors **************************************
    public Finder() {
        getJavaHome();
        if (null == this.javaHome || this.javaHome.isEmpty()) {
            this.javaHome = System.getProperty(Constants.JAVA_HOME_PROPERTY_KEY);
        }
        this.usedDistros = getUsedDistros();
    }


    // ******************** Methods *******************************************
    public Set<DistributionInfo> getDistributions(final List<String> searchPaths) {
        Set<DistributionInfo> distros = new HashSet<>();
        if (null == searchPaths || searchPaths.isEmpty()) { return distros; }

        if (service.isShutdown()) {
            service = Executors.newSingleThreadExecutor();
        }

        searchPaths.forEach(searchPath -> {
            final Path       path      = Paths.get(searchPath);
            final List<Path> javaFiles = findFileByName(path, javaFile);
            javaFiles.stream().forEach(java -> checkForDistribution(java.toString(), distros));
        });
        service.shutdown();
        try {
            service.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return distros;
    }

    public OperatingSystem getOperatingSystem() { return operatingSystem; }

    public Architecture getArchitecture() { return architecture; }

    public static final OperatingSystem detectOperatingSystem() {
        final String os = Constants.OS_NAME_PROPERTY.toLowerCase();
        if (os.indexOf("win") >= 0) {
            return OperatingSystem.WINDOWS;
        } else if (os.indexOf("mac") >= 0) {
            return OperatingSystem.MACOS;
        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            try {
                final ProcessBuilder processBuilder = new ProcessBuilder(DETECT_ALPINE_CMDS);
                final Process        process        = processBuilder.start();
                final String         result         = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
                return null == result ? OperatingSystem.LINUX : result.equals("1") ? OperatingSystem.ALPINE_LINUX : OperatingSystem.LINUX;
            } catch (IOException e) {
                e.printStackTrace();
                return OperatingSystem.LINUX;
            }
        } else if (os.indexOf("sunos") >= 0) {
            return OperatingSystem.SOLARIS;
        } else {
            return OperatingSystem.NOT_FOUND;
        }
    }

    public static final Architecture detectArchitecture() {
        final OperatingSystem operatingSystem = detectOperatingSystem();
        try {
            final ProcessBuilder processBuilder = OperatingSystem.WINDOWS == operatingSystem ? new ProcessBuilder(WIN_DETECT_ARCH_CMDS) : new ProcessBuilder(UX_DETECT_ARCH_CMDS);
            final Process        process        = processBuilder.start();
            final String         result         = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
            switch(operatingSystem) {
                case WINDOWS -> {
                    ARCHITECTURE_MATCHER.reset(result);
                    final List<MatchResult> results     = ARCHITECTURE_MATCHER.results().collect(Collectors.toList());
                    final int               noOfResults = results.size();
                    if (noOfResults > 0) {
                        final MatchResult   res = results.get(0);
                        return Architecture.fromText(res.group(2));
                    } else {
                        return Architecture.NOT_FOUND;
                    }
                }
                case MACOS -> {
                    return Architecture.fromText(result);
                }
                case LINUX -> {
                    return Architecture.fromText(result);
                }
            }

            // If not found yet try via system property
            final String arch = Constants.OS_ARCH_PROPERTY.toLowerCase();
            if (arch.contains("sparc")) return Architecture.SPARC;
            if (arch.contains("amd64") || arch.contains("86_64")) return Architecture.AMD64;
            if (arch.contains("86")) return Architecture.X86;
            if (arch.contains("s390x")) return Architecture.S390X;
            if (arch.contains("ppc64")) return Architecture.PPC64;
            if (arch.contains("arm") && arch.contains("64")) return Architecture.AARCH64;
            if (arch.contains("arm")) return Architecture.ARM;
            if (arch.contains("aarch64")) return Architecture.AARCH64;
            return Architecture.NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return Architecture.NOT_FOUND;
        }
    }

    public static final SysInfo getSysInfo() {
        final OperatingSystem operatingSystem = detectOperatingSystem();
        try {
            final ProcessBuilder processBuilder = OperatingSystem.WINDOWS == operatingSystem ? new ProcessBuilder(WIN_DETECT_ARCH_CMDS) : new ProcessBuilder(UX_DETECT_ARCH_CMDS);
            final Process        process        = processBuilder.start();
            final String         result         = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
            switch(operatingSystem) {
                case WINDOWS -> {
                    ARCHITECTURE_MATCHER.reset(result);
                    final List<MatchResult> results     = ARCHITECTURE_MATCHER.results().collect(Collectors.toList());
                    final int               noOfResults = results.size();
                    if (noOfResults > 0) {
                        final MatchResult   res = results.get(0);
                        return new SysInfo(operatingSystem, Architecture.fromText(res.group(2)), OperatingMode.NATIVE);
                    } else {
                        return new SysInfo(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NOT_FOUND);
                    }
                }
                case MACOS -> {
                    Architecture architecture = Architecture.fromText(result);
                    final ProcessBuilder processBuilder1 = new ProcessBuilder(MAC_DETECT_ROSETTA2_CMDS);
                    final Process        process1        = processBuilder1.start();
                    final String         result1         = new BufferedReader(new InputStreamReader(process1.getInputStream())).lines().collect(Collectors.joining("\n"));
                    return new SysInfo(operatingSystem, architecture, result1.equals("1") ? OperatingMode.EMULATED : OperatingMode.NATIVE);
                }
                case LINUX -> {
                    return new SysInfo(operatingSystem, Architecture.fromText(result), OperatingMode.NATIVE);
                }
            }

            // If not found yet try via system property
            final String arch = Constants.OS_ARCH_PROPERTY;
            if (arch.contains("sparc"))                           { return new SysInfo(operatingSystem, Architecture.SPARC, OperatingMode.NATIVE); }
            if (arch.contains("amd64") || arch.contains("86_64")) { return new SysInfo(operatingSystem, Architecture.AMD64, OperatingMode.NATIVE); }
            if (arch.contains("86"))                              { return new SysInfo(operatingSystem, Architecture.X86, OperatingMode.NATIVE); }
            if (arch.contains("s390x"))                           { return new SysInfo(operatingSystem, Architecture.S390X, OperatingMode.NATIVE); }
            if (arch.contains("ppc64"))                           { return new SysInfo(operatingSystem, Architecture.PPC64, OperatingMode.NATIVE); }
            if (arch.contains("arm") && arch.contains("64"))      { return new SysInfo(operatingSystem, Architecture.AARCH64, OperatingMode.NATIVE); }
            if (arch.contains("arm"))                             { return new SysInfo(operatingSystem, Architecture.ARM, OperatingMode.NATIVE); }
            if (arch.contains("aarch64"))                         { return new SysInfo(operatingSystem, Architecture.AARCH64, OperatingMode.NATIVE); }
            return new SysInfo(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NATIVE);
        } catch (IOException e) {
            e.printStackTrace();
            return new SysInfo(operatingSystem, Architecture.NOT_FOUND, OperatingMode.NATIVE);
        }
    }

    public static final String getDefaultSearchPath() {
        switch(detectOperatingSystem()) {
            case WINDOWS -> { return Finder.WINDOWS_JAVA_INSTALL_PATH; }
            case LINUX   -> { return Finder.LINUX_JAVA_INSTALL_PATH; }
            case MACOS   -> { return Finder.MACOS_JAVA_INSTALL_PATH; }
            default      -> { return ""; }
        }
    }

    public List<ProcessInfo> getUsedDistros() {
        final long              currentPid  = ProcessHandle.current().pid();
        final List<ProcessInfo> usedDistros = ProcessHandle.allProcesses()
                                                           .filter(process -> process.pid() != currentPid)
                                                           .filter(process -> process.info().command().isPresent())
                                                           .filter(process -> process.info().command().get().endsWith(this.javaFile))
                                                           .filter(process -> !Files.isSymbolicLink(Path.of(process.info().command().get())))
                                                           .map(process -> new ProcessInfo(process.pid(), process.info().command().get(), process.info().commandLine().isPresent() ? process.info().commandLine().get() : "unknown"))
                                                           .collect(Collectors.toList());
        return usedDistros;
    }

    private List<Path> findFileByName(final Path path, final String filename) {
        final List<Path> result = new ArrayList<>();
        try {
            Files.walkFileTree(path, new HashSet<>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)), Integer.MAX_VALUE, new SimpleFileVisitor<>() {
                @Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    final String name = file.getFileName().toString().toLowerCase();
                    if (filename.equals(name) && !Files.isSymbolicLink(file.toAbsolutePath())) { result.add(file); }
                    return FileVisitResult.CONTINUE;
                }
                @Override public FileVisitResult visitFileFailed(final Path file, final IOException e) throws IOException { return FileVisitResult.SKIP_SUBTREE; }
                @Override public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                    return Files.isSymbolicLink(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println(e);
            return result;
        }
        return result;
    }

    private void checkForDistribution(final String java, final Set<DistributionInfo> distros) {
        Instant       now    = Instant.now();
        AtomicBoolean inUse  = new AtomicBoolean(false);
        List<String>  usedBy = new ArrayList<>();

        try {
            List<String> commands = new ArrayList<>();
            commands.add(java);
            commands.add("-version");

            final String fileSeparator = File.separator;
            final String binFolder     = new StringBuilder(fileSeparator).append("bin").append(fileSeparator).append(".*").toString();

            ProcessBuilder builder  = new ProcessBuilder(commands).redirectErrorStream(true);
            Process process;
            try {
                process = builder.start();
            } catch (Exception e) {
                //System.out.println("Not allowed to check: " + java);
                return;
            }
            Streamer streamer = new Streamer(process.getInputStream(), d -> {
                final String parentPath      = OperatingSystem.WINDOWS == this.operatingSystem ? java.replaceAll("bin\\\\java.exe", "") : java.replaceAll(binFolder, fileSeparator);
                final File   releaseFile     = new File(parentPath + "release");
                String[]     lines           = d.split("\\|");
                String       name            = "Unknown build of OpenJDK";
                Distribution distribution    = Distribution.NOT_FOUND;
                String       apiString       = "";
                String       operatingSystem = "";
                String       architecture    = "";
                String       feature         = "";
                Boolean      fxBundled       = Boolean.FALSE;
                //FPU          fpu              = FPU.UNKNOWN;

                if (!this.javaHome.isEmpty() && !inUse.get() && parentPath.contains(javaHome)) {
                    inUse.set(true);
                }

                final File jreLibExtFolder = new File(new StringBuilder(parentPath).append("jre").append(fileSeparator).append("lib").append(fileSeparator).append("ext").toString());
                if (jreLibExtFolder.exists()) {
                    fxBundled = Stream.of(jreLibExtFolder.listFiles()).filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toSet()).stream().filter(filename -> filename.equalsIgnoreCase("jfxrt.jar")).count() > 0;
                }
                final File jmodsFolder = new File(new StringBuilder(parentPath).append("jmods").toString());
                if (jmodsFolder.exists()) {
                    fxBundled = Stream.of(jmodsFolder.listFiles()).filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toSet()).stream().filter(filename -> filename.startsWith("javafx")).count() > 0;
                }

                VersionNumber version    = null;
                VersionNumber jdkVersion = null;
                BuildScope    buildScope = BuildScope.BUILD_OF_OPEN_JDK;

                String line1         = lines[0];
                String line2         = lines[1];
                String withoutPrefix = line1;
                if (line1.startsWith("openjdk")) {
                    withoutPrefix = line1.replaceFirst("openjdk version", "");
                } else if (line1.startsWith("java")) {
                    withoutPrefix = line1.replaceFirst("java version", "");
                    // Find new GraalVM build (former enterprise edition)
                    if (line2.contains("GraalVM")) {
                        name         = "GraalVM";
                        apiString    = "graalvm";
                        buildScope   = BuildScope.BUILD_OF_GRAALVM;
                        distribution = Distribution.GRAALVM;
                    } else {
                        name       = "Oracle";
                        apiString  = "oracle";
                    }
                }

                // Find new GraalVM community builds
                if (!apiString.equals("graalvm") && line2.contains("jvmci")) {
                    VersionNumber newGraalVMBuild = VersionNumber.fromText("23.0-b12");
                    VersionNumber graalvmBuildFound = VersionNumber.fromText(line2.substring(line2.indexOf("jvmci"), line2.length() - 1).replace("jvmci-", ""));
                    if (graalvmBuildFound.compareTo(newGraalVMBuild) >= 0) {
                        name         = "GraalVM Community";
                        apiString    = "graalvm_community";
                        buildScope   = BuildScope.BUILD_OF_GRAALVM;
                        distribution = Distribution.GRAALVM_COMMUNITY;
                    }
                }

                if (line2.contains("Zulu")) {
                    name         = "Zulu";
                    apiString    = "zulu";
                    distribution = Distribution.ZULU;
                    ZULU_BUILD_MATCHER.reset(line2);
                    final List<MatchResult> results = ZULU_BUILD_MATCHER.results().collect(Collectors.toList());
                    if (!results.isEmpty()) {
                        MatchResult result = results.get(0);
                        version = VersionNumber.fromText(result.group(2));
                    }
                } else if (line2.contains("Zing") || line2.contains("Prime")) {
                    name         = "ZuluPrime";
                    apiString    = "zulu_prime";
                    distribution = Distribution.ZULU_PRIME;
                    final List<MatchResult> results = ZULU_BUILD_MATCHER.results().collect(Collectors.toList());
                    if (!results.isEmpty()) {
                        MatchResult result = results.get(0);
                        version = VersionNumber.fromText(result.group(2));
                    }
                } else if (line2.contains("Semeru")) {
                    if (line2.contains("Certified")) {
                        name         = "Semeru certified";
                        apiString    = "semeru_certified";
                        distribution = Distribution.SEMERU_CERTIFIED;
                    } else {
                        name         = "Semeru";
                        apiString    = "semeru";
                        distribution = Distribution.SEMERU;
                    }
                } else if (line2.contains("Tencent")) {
                    name         = "Kona";
                    apiString    = "kona";
                    distribution = Distribution.KONA;
                } else if (line2.contains("Bisheng")) {
                    name         = "Bishenq";
                    apiString    = "bisheng";
                    distribution = Distribution.BISHENG;
                }

                if (null == version) {
                    final String versionNumberText = withoutPrefix.substring(withoutPrefix.indexOf("\"") + 1, withoutPrefix.lastIndexOf("\""));
                    final Semver semver            = Semver.fromText(versionNumberText).getSemver1();
                    version = VersionNumber.fromText(semver.toString(true));
                }
                VersionNumber graalVersion = version;

                releaseProperties.clear();
                if (releaseFile.exists()) {
                    try (FileInputStream propFile = new FileInputStream(releaseFile)) {
                        releaseProperties.load(propFile);
                    } catch (IOException ex) {
                        System.out.println("Error reading release properties file. " + ex);
                    }
                    if (!releaseProperties.isEmpty()) {
                        if (releaseProperties.containsKey("IMPLEMENTOR") && name.equals(Constants.UNKNOWN_BUILD_OF_OPENJDK)) {
                            switch (releaseProperties.getProperty("IMPLEMENTOR").replaceAll("\"", "")) {
                                case "AdoptOpenJDK" -> { name         = "Adopt OpenJDK";
                                                         apiString    = "aoj";
                                                         distribution = Distribution.AOJ;
                                }
                                case "Alibaba" -> { name         = "Dragonwell";
                                                    apiString    = "dragonwell";
                                                    distribution = Distribution.DRAGONWELL;
                                }
                                case "Amazon.com Inc." -> { name         = "Corretto";
                                                            apiString    = "corretto";
                                                            distribution = Distribution.CORRETTO;
                                }
                                case "Azul Systems, Inc." -> {
                                    if (releaseProperties.containsKey("IMPLEMENTOR_VERSION")) {
                                        final String implementorVersion = releaseProperties.getProperty("IMPLEMENTOR_VERSION");
                                        if (implementorVersion.startsWith("Zulu")) {
                                            name         = "Zulu";
                                            apiString    = "zulu";
                                            distribution = Distribution.ZULU;
                                        } else if (implementorVersion.startsWith("Zing") || implementorVersion.startsWith("Prime")) {
                                            name         = "ZuluPrime";
                                            apiString    = "zulu_prime";
                                            distribution = Distribution.ZULU_PRIME;
                                        }
                                    }
                                }
                                case "mandrel" -> { name         = "Mandrel";
                                                    apiString    = "mandrel";
                                                    distribution = Distribution.MANDREL;
                                }
                                case "Microsoft" -> { name         = "Microsoft";
                                                      apiString    = "microsoft";
                                                      distribution = Distribution.MICROSOFT;
                                }
                                case "ojdkbuild" -> { name         = "OJDK Build";
                                                      apiString    = "ojdk_build";
                                                      distribution = Distribution.OJDK_BUILD;
                                }
                                case "Oracle Corporation" -> { name         = "Oracle OpenJDK";
                                                               apiString    = "oracle_openjdk";
                                                               distribution = Distribution.ORACLE_OPEN_JDK;
                                }
                                case "Red Hat, Inc." -> { name         = "Red Hat";
                                                          apiString    = "redhat";
                                                          distribution = Distribution.RED_HAT;
                                }
                                case "SAP SE" -> { name         = "SAP Machine";
                                                   apiString    = "sap_machine";
                                                   distribution = Distribution.SAP_MACHINE;
                                }
                                case "OpenLogic" -> { name         = "OpenLogic";
                                                      apiString    = "openlogic";
                                                      distribution = Distribution.OPEN_LOGIC;
                                }
                                case "JetBrains s.r.o." -> { name         = "JetBrains";
                                                             apiString    = "jetbrains";
                                                             distribution = Distribution.JETBRAINS;
                                }
                                case "Eclipse Foundation" -> { name         = "Temurin";
                                                               apiString    = "temurin";
                                                               distribution = Distribution.TEMURIN;
                                }
                                case "Tencent" -> { name         = "Kona";
                                                    apiString    = "kona";
                                                    distribution = Distribution.KONA;
                                }
                                case "Bisheng" -> { name         = "Bisheng";
                                                    apiString    = "bisheng";
                                                    distribution = Distribution.BISHENG;
                                }
                                case "Debian" -> { name         = "Debian";
                                                   apiString    = "debian";
                                                   distribution = Distribution.DEBIAN;
                                }
                                case "Ubuntu" -> { name         = "Ubuntu";
                                                   apiString    = "ubuntu";
                                                   distribution = Distribution.UBUNTU;
                                }
                                case "N/A" -> { }/* Unknown */
                            }
                        }
                        if (releaseProperties.containsKey("OS_ARCH")) {
                            architecture = releaseProperties.getProperty("OS_ARCH").toLowerCase().replaceAll("\"", "");
                        }
                        if (releaseProperties.containsKey("JVM_VARIANT")) {
                            if (name == "Adopt OpenJDK") {
                                String jvmVariant = releaseProperties.getProperty("JVM_VARIANT").toLowerCase().replaceAll("\"", "");
                                if (jvmVariant.equals("dcevm")) {
                                    name         = "Trava OpenJDK";
                                    apiString    = "trava";
                                    distribution = Distribution.TRAVA;
                                } else if (jvmVariant.equals("openj9")) {
                                    name         = "Adopt OpenJDK J9";
                                    apiString    = "aoj_openj9";
                                    distribution = Distribution.AOJ_OPENJ9;
                                }
                            }
                        }
                        if (releaseProperties.containsKey("OS_NAME")) {
                            switch (releaseProperties.getProperty("OS_NAME").toLowerCase().replaceAll("\"", "")) {
                                case "darwin" -> operatingSystem = "macos";
                                case "linux" -> operatingSystem = "linux";
                                case "windows" -> operatingSystem = "windows";
                            }
                        }
                        if (releaseProperties.containsKey("MODULES") && !fxBundled) {
                            fxBundled = (releaseProperties.getProperty("MODULES").contains("javafx"));
                        }
                    /*
                    if (releaseProperties.containsKey("SUN_ARCH_ABI")) {
                        String abi = releaseProperties.get("SUN_ARCH_ABI").toString();
                        switch (abi) {
                            case "gnueabi"   -> fpu = FPU.SOFT_FLOAT;
                            case "gnueabihf" -> fpu = FPU.HARD_FLOAT;
                        }
                    }
                    */
                    }
                }

                if (lines.length > 2) {
                    String line3 = lines[2].toLowerCase();
                    for (String feat : Constants.FEATURES) {
                        if (line3.contains(feat)) {
                            feature = feat;
                            break;
                        }
                    }

                }

                if (name.equalsIgnoreCase("Mandrel")) {
                    buildScope = BuildScope.BUILD_OF_GRAALVM;
                    if (releaseProperties.containsKey("JAVA_VERSION")) {
                        final String javaVersion = releaseProperties.getProperty("JAVA_VERSION");
                        if (null == jdkVersion) { jdkVersion = VersionNumber.fromText(javaVersion); }
                    }
                }

                if (name.equals(Constants.UNKNOWN_BUILD_OF_OPENJDK) && lines.length > 2) {
                    String line3      = lines[2].toLowerCase();
                    File   readmeFile = new File(parentPath + "readme.txt");
                    if (readmeFile.exists()) {
                        try {
                            List<String> readmeLines = Helper.readTextFileToList(readmeFile.getAbsolutePath());
                            if (readmeLines.stream().filter(l -> l.toLowerCase().contains("liberica native image kit")).count() > 0) {
                                name         = "Liberica Native";
                                apiString    = "liberica_native";
                                distribution = Distribution.LIBERICA_NATIVE;
                                buildScope   = BuildScope.BUILD_OF_GRAALVM;

                                GRAALVM_VERSION_MATCHER.reset(line3);
                                final List<MatchResult> results = GRAALVM_VERSION_MATCHER.results().collect(Collectors.toList());
                                if (!results.isEmpty()) {
                                    MatchResult result = results.get(0);
                                    version = VersionNumber.fromText(result.group(2));
                                }
                                if (releaseProperties.containsKey("JAVA_VERSION")) {
                                    final String javaVersion = releaseProperties.getProperty("JAVA_VERSION");
                                    if (null == jdkVersion) { jdkVersion = VersionNumber.fromText(javaVersion); }
                                }
                            } else if (readmeLines.stream().filter(l -> l.toLowerCase().contains("liberica")).count() > 0) {
                                name         = "Liberica";
                                apiString    = "liberica";
                                distribution = Distribution.LIBERICA;
                            }
                        } catch (IOException e) {

                        }
                    } else {
                        if (line3.contains("graalvm") && !apiString.equals("graalvm_community") && !apiString.equals("graalvm")) {
                            name = "GraalVM CE";
                            String distroPreFix = "graalvm_ce";
                            if (releaseProperties.containsKey("IMPLEMENTOR")) {
                                switch (releaseProperties.getProperty("IMPLEMENTOR").replaceAll("\"", "")) {
                                    case "GraalVM Community" -> {
                                        name         = "GraalVM CE";
                                        distroPreFix = "graalvm_ce";
                                        distribution = Distribution.GRAALVM_CE;
                                    }
                                    case "GraalVM Enterprise" -> {
                                        name         = "GraalVM";
                                        distroPreFix = "graalvm";
                                        distribution = Distribution.GRAALVM_EE;
                                    }
                                }
                            }
                            apiString  = graalVersion.getMajorVersion().getAsInt() >= 8 ? distroPreFix + graalVersion.getMajorVersion().getAsInt() : "";
                            buildScope = BuildScope.BUILD_OF_GRAALVM;

                            GRAALVM_VERSION_MATCHER.reset(line3);
                            final List<MatchResult> results = GRAALVM_VERSION_MATCHER.results().collect(Collectors.toList());
                            if (!results.isEmpty()) {
                                MatchResult result = results.get(0);
                                version = VersionNumber.fromText(result.group(2));
                            }

                            if (releaseProperties.containsKey("VENDOR")) {
                                final String vendor = releaseProperties.getProperty("VENDOR").toLowerCase().replaceAll("\"", "");
                                if (vendor.equalsIgnoreCase("Gluon")) {
                                    name         = "Gluon GraalVM CE";
                                    apiString    = "gluon_graalvm";
                                    distribution = Distribution.GLUON_GRAALVM;
                                }
                            }
                            if (releaseProperties.containsKey("JAVA_VERSION")) {
                                final String javaVersion = releaseProperties.getProperty("JAVA_VERSION");
                                if (null == jdkVersion) { jdkVersion = VersionNumber.fromText(javaVersion); }
                            }
                        } else if (line3.contains("microsoft")) {
                            name         = "Microsoft";
                            apiString    = "microsoft";
                            distribution = Distribution.MICROSOFT;
                        } else if (line3.contains("corretto")) {
                            name         = "Corretto";
                            apiString    = "corretto";
                            distribution = Distribution.CORRETTO;
                        } else if (line3.contains("temurin")) {
                            name         = "Temurin";
                            apiString    = "temurin";
                            distribution = Distribution.TEMURIN;
                        }
                    }
                }

                if (null == jdkVersion) { jdkVersion = version; }

                if (architecture.isEmpty()) { architecture = this.architecture.name().toLowerCase(); }

                // Check if found distro is in use
                for (ProcessInfo processInfo : usedDistros) {
                    if (java.contains(processInfo.cmd())) {
                        inUse.set(true);
                        usedBy.add(processInfo.cmdLine());
                        break;
                    }
                }

                DistributionInfo distributionFound = new DistributionInfo(now, distribution, name, apiString, version.toString(OutputFormat.REDUCED_COMPRESSED, true, true), Integer.toString(jdkVersion.getMajorVersion().getAsInt()), operatingSystem, architecture, fxBundled, parentPath, feature, buildScope, inUse.get(), usedBy);

                distros.add(distributionFound);
            });
            service.submit(streamer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getJavaHome() {
        try {
            ProcessBuilder processBuilder = OperatingSystem.WINDOWS == operatingSystem ? new ProcessBuilder(WIN_JAVA_HOME_CMDS) : OperatingSystem.MACOS == operatingSystem ? new ProcessBuilder(MAC_JAVA_HOME_CMDS) : new ProcessBuilder(LINUX_JAVA_HOME_CMDS);
            Process        process        = processBuilder.start();
            Streamer       streamer       = new Streamer(process.getInputStream(), d -> this.javaHome = d);
            service.submit(streamer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Streamer implements Runnable {
        private InputStream      inputStream;
        private Consumer<String> consumer;

        public Streamer(final InputStream inputStream, final Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer    = consumer;
        }

        @Override public void run() {
            final StringBuilder builder = new StringBuilder();
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(line -> builder.append(line).append("|"));
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            consumer.accept(builder.toString());
        }
    }
}
