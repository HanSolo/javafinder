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

public enum OutputType {
    BEAUTIFIED_JSON, JSON, CSV, NOT_FOUND;

    public static final OutputType fromText(final String text) {
        if (null == text || text.isEmpty()) { return NOT_FOUND; }
        switch(text) {
            case "json", "JSON", "Json"               -> { return JSON; }
            case "beautified_json", "BEAUTIFIED_JSON" -> { return BEAUTIFIED_JSON; }
            case "csv", "CSV"                         -> { return CSV; }
            default                                   -> { return NOT_FOUND; }
        }
    }
}
