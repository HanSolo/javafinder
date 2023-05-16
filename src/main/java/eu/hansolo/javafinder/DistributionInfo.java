package eu.hansolo.javafinder;

import eu.hansolo.jdktools.scopes.BuildScope;
import eu.hansolo.jdktools.util.OutputFormat;

import static eu.hansolo.javafinder.Constants.*;
import static eu.hansolo.javafinder.OutputType.CSV;
import static eu.hansolo.javafinder.OutputType.JSON;
import static eu.hansolo.jdktools.Constants.*;


public record DistributionInfo(Distribution distribution, String name, String apiString, String version, String jdkMajorVersion, String operatingSystem, String architecture, Boolean fxBundled, String location, String feature, BuildScope buildScope) {

    public boolean isBuildOfOpenJDK() { return buildScope == BuildScope.BUILD_OF_OPEN_JDK; }

    public boolean isBuildOfGraalVM() { return buildScope == BuildScope.BUILD_OF_GRAALVM; }

    public String toString(final OutputType outputType) throws IllegalArgumentException {
        if (null == outputType) { throw new IllegalArgumentException("outputType cannot be null"); }
        final StringBuilder msgBuilder = new StringBuilder();
        switch(outputType) {
            case CSV -> {
                return msgBuilder.append(distribution.getVendor().getUiString())
                                 .append(COMMA)
                                 .append(distribution.getUiString())
                                 .append(COMMA)
                                 .append(version())
                                 .append(COMMA)
                                 .append(location())
                                 .append(COMMA)
                                 .append(buildScope().getUiString())
                                 .append(NEW_LINE)
                                 .toString();
            }
            default  -> {
                return msgBuilder.append(CURLY_BRACKET_OPEN)
                                 .append(QUOTES).append(FIELD_VENDOR).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getVendor().getUiString()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_NAME).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getUiString()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_VERSION).append(QUOTES).append(COLON).append(QUOTES).append(version()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_PATH).append(QUOTES).append(COLON).append(QUOTES).append(location()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_BUILD_SCOPE).append(QUOTES).append(COLON).append(QUOTES).append(buildScope().getUiString()).append(QUOTES)
                                 .append(CURLY_BRACKET_CLOSE).toString();
            }
        }
    }

    @Override public String toString() {
        return toString(JSON);
    }
}
