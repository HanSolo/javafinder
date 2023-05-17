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


public enum Vendor {
    ALIBABA("Alibaba", "alibaba"),
    AMAZON("Amazon", "amazon"),
    AZUL("Azul", "azul"),
    BELL_SOFT("BellSoft", "bellsoft"),
    COMMUNITY("Community", "community"),
    ECLIPSE("Eclipse", "eclipse"),
    GLUON("Gluon", "gluon"),
    HUAWEI("Huawei", "huawei"),
    IBM("IBM", "ibm"),
    JETBRAINS("JetBrains", "jetbrains"),
    MICROSOFT("Microsoft", "microsoft"),
    OPEN_LOGIC("OpenLogic", "open_logic"),
    ORACLE("Oracle", "oracle"),
    RED_HAT("RedHat", "redhat"),
    SAP("SAP", "sap"),
    TENCENT("Tencent", "tencent"),
    NONE("", ""),
    NOT_FOUND("-", "-");


    private final String uiString;
    private final String apiString;


    // ******************** Constructors **************************************
    Vendor(final String uiString, final String apiString) {
        this.uiString  = uiString;
        this.apiString = apiString;
    }


    // ******************** Methods *******************************************
    public final String getUiString() { return uiString; }

    public final String getApiString() { return apiString; }

    public static final Vendor fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch(text) {
            case "alibaba", "ALIBABA", "Alibaba"                                   -> { return ALIBABA; }
            case "amazon", "AMAZON", "Amazon"                                      -> { return AMAZON; }
            case "azul", "AZUL", "Azul"                                            -> { return AZUL; }
            case "bellsoft", "BELLSOFT", "BellSoft"                                -> { return BELL_SOFT; }
            case "community", "COMMUNITY", "Community"                             -> { return COMMUNITY; }
            case "eclipse", "ECLIPSE", "Eclipse"                                   -> { return ECLIPSE; }
            case "huawei", "HUAWEI", "Huawei"                                      -> { return HUAWEI; }
            case "ibm", "IBM"                                                      -> { return IBM; }
            case "jetbrains", "JETBRAINS", "JetBrains"                             -> { return JETBRAINS; }
            case "microsoft", "MICROSOFT", "Microsoft"                             -> { return MICROSOFT; }
            case "open_logic", "OPEN_LOGIC", "OpenLogic", "openlogic", "OPENLOGIC" -> { return OPEN_LOGIC; }
            case "oracle", "ORACLE", "Oracle"                                      -> { return ORACLE; }
            case "redhat", "REDHAT", "RedHat", "red_hat", "RED_HAT", "Red Hat"     -> { return RED_HAT; }
            case "sap", "SAP"                                                      -> { return SAP; }
            case "tencent", "TENCENT", "Tencent"                                   -> { return TENCENT; }
            default                                                                -> { return NOT_FOUND; }
        }
    }
}
