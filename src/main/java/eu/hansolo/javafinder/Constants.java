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

import java.util.List;


public class Constants {
    public static final String       UNKNOWN_BUILD_OF_OPENJDK = "Unknown build of OpenJDK";
    public static final List<String> FEATURES                 = List.of("loom", "panama", "metropolis", "valhalla", "lanai", "kona_fiber", "crac");
    public static final String       FIELD_VENDOR             = "vendor";
    public static final String       FIELD_NAME               = "name";
    public static final String       FIELD_VERSION            = "version";
    public static final String       FIELD_PATH               = "path";
    public static final String       FIELD_BUILD_SCOPE        = "build_scope";
    public static final String       FIELD_DISTRIBUTIONS      = "distributions";
    public static final String       FIELD_SYSINFO            = "sysinfo";
    public static final String       FIELD_ARCHITECTURE       = "architecture";
    public static final String       FIELD_BIT                = "bit";
    public static final String       FIELD_OPERATING_SYSTEM   = "operating_system";
    public static final String       FIELD_SEARCH_PATH        = "search_path";
    public static final String       FIELD_CREATED_BY         = "created_by";
    public static final String       FIELD_BUILD_JDK          = "build_jdk";
    public static final String       JAVA_HOME_PROPERTY_KEY   = "java.home";
    public static final String       OS_NAME_PROPERTY_KEY     = "os.name";
    public static final String       OS_ARCH_PROPERTY_KEY     = "os.arch";
    public static final String       JAVA_HOME_PROPERTY;
    public static final String       OS_NAME_PROPERTY;
    public static final String       OS_ARCH_PROPERTY;

    static {
        JAVA_HOME_PROPERTY = System.getProperty(JAVA_HOME_PROPERTY_KEY);
        OS_NAME_PROPERTY   = System.getProperty(OS_NAME_PROPERTY_KEY);
        OS_ARCH_PROPERTY   = System.getProperty(OS_ARCH_PROPERTY_KEY);
    }
}
