package eu.hansolo.javafinder;

import static eu.hansolo.javafinder.Constants.FIELD_ARCHITECTURE;
import static eu.hansolo.javafinder.Constants.FIELD_BIT;
import static eu.hansolo.javafinder.Constants.FIELD_BUILD_JDK;
import static eu.hansolo.javafinder.Constants.FIELD_CREATED_BY;
import static eu.hansolo.javafinder.Constants.FIELD_OPERATING_SYSTEM;
import static eu.hansolo.javafinder.Constants.FIELD_SYSINFO;
import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public record JdkInfo(String createdBy, String buildJdk) {

    public String toString(final OutputType outputType) throws IllegalArgumentException {
        if (null == outputType) { throw new IllegalArgumentException("outputType cannot be null"); }
        switch(outputType) {
            case CSV -> {
                return new StringBuilder().append(createdBy)
                                          .append(COMMA)
                                          .append(buildJdk)
                                          .append(NEW_LINE)
                                          .toString();
            }
            default -> {
                return new StringBuilder().append(CURLY_BRACKET_OPEN)
                                          .append(QUOTES).append(FIELD_CREATED_BY).append(QUOTES).append(COLON).append(QUOTES).append(buildJdk).append(QUOTES).append(COMMA)
                                          .append(QUOTES).append(FIELD_BUILD_JDK).append(QUOTES).append(COLON).append(QUOTES).append(createdBy).append(QUOTES)
                                          .append(CURLY_BRACKET_CLOSE)
                                          .toString();
            }
        }
    }

    @Override public String toString() {
        return toString(OutputType.JSON);
    }
}
