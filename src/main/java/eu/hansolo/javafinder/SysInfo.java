package eu.hansolo.javafinder;

import eu.hansolo.jdktools.Architecture;
import eu.hansolo.jdktools.OperatingMode;
import eu.hansolo.jdktools.OperatingSystem;
import eu.hansolo.jdktools.util.OutputFormat;

import static eu.hansolo.javafinder.Constants.*;
import static eu.hansolo.jdktools.Constants.*;


public record SysInfo(OperatingSystem operatingSystem, Architecture architecture, OperatingMode operatingMode) {

    public String toString(final OutputType outputType) throws IllegalArgumentException {
        if (null == outputType) { throw new IllegalArgumentException("outputType cannot be null"); }
        switch (outputType) {
            case CSV -> {
                return new StringBuilder().append(operatingSystem().getUiString())
                                          .append(COMMA)
                                          .append(architecture().getUiString())
                                          .append(COMMA)
                                          .append(architecture().getBitness().getUiString())
                                          .append(NEW_LINE)
                                          .toString();
            }
            default -> {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(operatingSystem().getUiString()).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getUiString()).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getBitness().getUiString()).append(QUOTES)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
            }
        }
    }

    @Override public String toString() {
        return toString(OutputType.JSON);
    }
}
