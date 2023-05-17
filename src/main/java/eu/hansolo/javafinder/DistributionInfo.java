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

import static eu.hansolo.javafinder.Constants.FIELD_BUILD_SCOPE;
import static eu.hansolo.javafinder.Constants.FIELD_NAME;
import static eu.hansolo.javafinder.Constants.FIELD_PATH;
import static eu.hansolo.javafinder.Constants.FIELD_VENDOR;
import static eu.hansolo.javafinder.Constants.FIELD_VERSION;
import static eu.hansolo.javafinder.OutputType.JSON;
import static eu.hansolo.jdktools.Constants.COLON;
import static eu.hansolo.jdktools.Constants.COMMA;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_CLOSE;
import static eu.hansolo.jdktools.Constants.CURLY_BRACKET_OPEN;
import static eu.hansolo.jdktools.Constants.NEW_LINE;
import static eu.hansolo.jdktools.Constants.QUOTES;


public record DistributionInfo(Distribution distribution, String name, String apiString, String version, String jdkMajorVersion, String operatingSystem, String architecture, Boolean fxBundled, String location, String feature, BuildScope buildScope) {

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
                                 .append(location())
                                 .append(COMMA)
                                 .append(buildScope().getUiString().substring(buildScope.getUiString().lastIndexOf(" ") + 1))
                                 .append(NEW_LINE)
                                 .toString();
            }
            default  -> {
                return msgBuilder.append(CURLY_BRACKET_OPEN)
                                 .append(QUOTES).append(FIELD_VENDOR).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getVendor().getUiString()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_NAME).append(QUOTES).append(COLON).append(QUOTES).append(distribution().getUiString()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_VERSION).append(QUOTES).append(COLON).append(QUOTES).append(version()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_PATH).append(QUOTES).append(COLON).append(QUOTES).append(location()).append(QUOTES).append(COMMA)
                                 .append(QUOTES).append(FIELD_BUILD_SCOPE).append(QUOTES).append(COLON).append(QUOTES).append(buildScope().getUiString().substring(buildScope.getUiString().lastIndexOf(" ") + 1)).append(QUOTES)
                                 .append(CURLY_BRACKET_CLOSE).toString();
            }
        }
    }

    @Override public String toString() {
        return toString(JSON);
    }
}
