package eu.hansolo.javafinder;

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.OperatingMode;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.util.OutputFormat;

import static eu.hansolo.javafinder.Constants.*;
import static eu.hansolo.jdktools.Constants.*;


public record SysInfo(OperatingSystem operatingSystem, Architecture architecture, OperatingMode operatingMode) {

    public String toString(final OutputFormat outputFormat) {
        switch (outputFormat) {
            case FULL -> {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(INDENTED_QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                                          .append(INDENTED_QUOTES).append(INDENTED_QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(operatingSystem().getApiString()).append(QUOTES).append(COMMA_NEW_LINE)
                                          .append(INDENTED_QUOTES).append(INDENTED_QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getApiString()).append(QUOTES).append(COMMA_NEW_LINE)
                                          .append(INDENTED_QUOTES).append(INDENTED_QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getBitness().getApiString()).append(QUOTES).append(NEW_LINE)
                                          .append(INDENTED_QUOTES).append(CURLY_BRACKET_CLOSE).append(NEW_LINE)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
            }
            default -> {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(operatingSystem().getApiString()).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getApiString()).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getBitness().getApiString()).append(QUOTES)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
            }
        }
    }

    @Override public String toString() {
        return toString(OutputFormat.FULL_COMPRESSED);
    }
}
