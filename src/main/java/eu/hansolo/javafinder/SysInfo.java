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

import java.util.Map;

import static eu.hansolo.javafinder.Constants.*;
import static eu.hansolo.jdktools.Constants.*;


public record SysInfo(OperatingSystem operatingSystem, Architecture architecture, OperatingMode operatingMode, String hostname, Map<String,String> envVariables) {

    // ******************** Methods *******************************************
    public String toString(final OutputType outputType) throws IllegalArgumentException {
        if (null == outputType) { throw new IllegalArgumentException("outputType cannot be null"); }

        switch (outputType) {
            case CSV -> {
                return new StringBuilder().append(operatingSystem().getUiString())
                                          .append(COMMA)
                                          .append(architecture().getUiString())
                                          .append(COMMA)
                                          .append(architecture().getBitness().getUiString())
                                          .append(COMMA)
                                          .append(hostname())
                                          .append(NEW_LINE)
                                          .toString();
            }
            default -> {
                final StringBuilder msgBuilder = new StringBuilder();
                msgBuilder.append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append(FIELD_SYSINFO).append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN)
                          .append(QUOTES).append(FIELD_OPERATING_SYSTEM).append(QUOTES).append(COLON).append(QUOTES).append(operatingSystem().getUiString()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_ARCHITECTURE).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getUiString()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_BIT).append(QUOTES).append(COLON).append(QUOTES).append(architecture().getBitness().getUiString()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append(FIELD_HOSTNAME).append(QUOTES).append(COLON).append(QUOTES).append(hostname()).append(QUOTES).append(COMMA)
                          .append(QUOTES).append("environt_variables").append(QUOTES).append(COLON).append(CURLY_BRACKET_OPEN);

                envVariables.entrySet().forEach(entry -> msgBuilder.append(QUOTES).append(entry.getKey()).append(QUOTES).append(COLON).append(QUOTES).append(entry.getValue()).append(QUOTES).append(COMMA));
                if (!envVariables.isEmpty()) { msgBuilder.setLength(msgBuilder.length() - 1); }

                msgBuilder.append(CURLY_BRACKET_CLOSE)
                          .append(CURLY_BRACKET_CLOSE)
                          .append(CURLY_BRACKET_CLOSE);

                return msgBuilder.toString();
            }
        }
    }

    @Override public String toString() {
        return toString(OutputType.JSON);
    }
}
