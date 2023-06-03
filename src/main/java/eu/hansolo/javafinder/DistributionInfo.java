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

import eu.hansolo.jdktools.scopes.BuildScope;

import java.time.Instant;
import java.util.List;

import static eu.hansolo.javafinder.Constants.FIELD_BUILD_SCOPE;
import static eu.hansolo.javafinder.Constants.FIELD_IN_USE;
import static eu.hansolo.javafinder.Constants.FIELD_NAME;
import static eu.hansolo.javafinder.Constants.FIELD_PATH;
import static eu.hansolo.javafinder.Constants.FIELD_TIMESTAMP;
import static eu.hansolo.javafinder.Constants.FIELD_USED_BY;
import static eu.hansolo.javafinder.Constants.FIELD_VENDOR;
import static eu.hansolo.javafinder.Constants.FIELD_VERSION;
import static eu.hansolo.javafinder.OutputType.JSON;
import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.COMMA_NEW_LINE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.INDENT;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.SQUARE_BRACKET_OPEN;


public record DistributionInfo(Instant timestamp, Distribution distribution, String name, String apiString, String version, String jdkMajorVersion, String operatingSystem, String architecture, Boolean fxBundled, String location, String feature, BuildScope buildScope, boolean inUse, List<String> usedBy) {

    // ******************** Methods *******************************************
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
                                 .append(timestamp())
                                 .append(COMMA)
                                 .append(location())
                                 .append(COMMA)
                                 .append(buildScope().getUiString().substring(buildScope.getUiString().lastIndexOf(" ") + 1))
                                 .append(COMMA)
                                 .append(inUse())
                                 .append(COMMA)
                                 .append(timestamp().getEpochSecond())
                                 .append(NEW_LINE)
                                 .toString();
            }
            case BEAUTIFIED_JSON -> {
                msgBuilder.append(INDENT).append(INDENT).append(CURLY_BRACKET_OPEN).append(NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_VENDOR).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getVendor().getUiString()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_NAME).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getUiString()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_VERSION).append(QUOTES).append(COLON).append(QUOTES).append(version()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_TIMESTAMP).append(QUOTES).append(COLON).append(timestamp().getEpochSecond()).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_PATH).append(QUOTES).append(COLON).append(QUOTES).append(location()).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_BUILD_SCOPE).append(QUOTES).append(COLON).append(QUOTES).append(buildScope().getUiString().substring(buildScope.getUiString().lastIndexOf(" ") + 1)).append(QUOTES).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_IN_USE).append(QUOTES).append(COLON).append(inUse()).append(COMMA_NEW_LINE)
                          .append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(FIELD_USED_BY).append(QUOTES).append(COLON).append(SQUARE_BRACKET_OPEN).append(NEW_LINE);

                usedBy().forEach(cmdLine -> {
                    msgBuilder.append(INDENT).append(INDENT).append(INDENT).append(INDENT).append(QUOTES).append(cmdLine).append(QUOTES).append(COMMA_NEW_LINE);
                });
                if (!usedBy().isEmpty()) { msgBuilder.setLength(msgBuilder.length() - 2); }

                msgBuilder.append(INDENT).append(INDENT).append(INDENT).append(SQUARE_BRACKET_CLOSE).append(NEW_LINE)
                          .append(INDENT).append(INDENT).append(CURLY_BRACKET_CLOSE);
                return msgBuilder.toString();
            }
            default  -> {
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append(FIELD_VENDOR).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getVendor().getUiString()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_NAME).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getUiString()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_VERSION).append(QUOTES).append(COLON).append(QUOTES).append(version()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_TIMESTAMP).append(QUOTES).append(COLON).append(timestamp().getEpochSecond()).append(COMMA)
                          .append(QUOTES).append(FIELD_PATH).append(QUOTES).append(COLON).append(QUOTES).append(location()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_BUILD_SCOPE).append(QUOTES).append(COLON).append(QUOTES).append(buildScope().getUiString().substring(buildScope.getUiString().lastIndexOf(" ") + 1)).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_IN_USE).append(QUOTES).append(COLON).append(inUse()).append(COMMA)
                          .append(QUOTES).append(FIELD_USED_BY).append(QUOTES).append(COLON).append(SQUARE_BRACKET_OPEN);
                usedBy().forEach(cmdLine -> {
                    msgBuilder.append(QUOTES).append(cmdLine).append(QUOTES).append(COMMA);
                });
                          if (!usedBy().isEmpty()) { msgBuilder.setLength(msgBuilder.length() - 1); }
                msgBuilder.append(SQUARE_BRACKET_CLOSE)
                          .append(CURLY_BRACKET_CLOSE);
                return msgBuilder.toString();
            }
        }
    }

    @Override public String toString() {
        return toString(JSON);
    }
}
