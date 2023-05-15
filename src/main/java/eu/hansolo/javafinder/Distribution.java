package eu.hansolo.javafinder;

import eu.hansolo.jdktools.scopes.BuildScope;
import eu.hansolo.jdktools.scopes.Scope;


public enum Distribution {
    AOJ("AOJ", "aoj", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    AOJ_OPENJ9("AOJ OpenJ9", "aoj_openj9", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    AZURE_ZULU("Azure Zulu", "azure_zulu", Vendor.AZUL, BuildScope.BUILD_OF_OPEN_JDK),
    BISHENG("Bi Sheng", "bisheng", Vendor.HUAWEI, BuildScope.BUILD_OF_OPEN_JDK),
    CORRETTO("Corretto", "corretto", Vendor.AMAZON, BuildScope.BUILD_OF_OPEN_JDK),
    DEBIAN("Debian", "debian", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    DRAGONWELL("Dragonwell", "dragonwell", Vendor.ALIBABA, BuildScope.BUILD_OF_OPEN_JDK),
    GLUON_GRAALVM("Gluon GraalVM", "gluon_graalvm", Vendor.GLUON, BuildScope.BUILD_OF_GRAALVM),
    GRAALVM_CE("Graal VM CE", "graalvm_ce", Vendor.ORACLE, BuildScope.BUILD_OF_GRAALVM),
    GRAALVM_EE("Graal VM EE", "graalvm_ee", Vendor.ORACLE, BuildScope.BUILD_OF_GRAALVM),
    JETBRAINS("JetBrains", "jetbrains", Vendor.JETBRAINS, BuildScope.BUILD_OF_OPEN_JDK),
    KONA("Kona", "kona", Vendor.TENCENT, BuildScope.BUILD_OF_OPEN_JDK),
    LIBERICA("Liberica", "liberica", Vendor.BELL_SOFT, BuildScope.BUILD_OF_OPEN_JDK),
    LIBERICA_NATIVE("Liberica Native", "liberica_native", Vendor.BELL_SOFT, BuildScope.BUILD_OF_GRAALVM),
    MANDREL("Mandrel", "mandrel", Vendor.RED_HAT, BuildScope.BUILD_OF_GRAALVM),
    MICROSOFT("Microsoft", "microsoft", Vendor.MICROSOFT, BuildScope.BUILD_OF_OPEN_JDK),
    OJDK_BUILD("OJDKBuild", "ojdk_build", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    OPEN_LOGIC("OpenLogic", "openlogic", Vendor.OPEN_LOGIC, BuildScope.BUILD_OF_OPEN_JDK),
    ORACLE_OPEN_JDK("Oracle OpenJDK", "oracle_open_jdk", Vendor.ORACLE, BuildScope.BUILD_OF_OPEN_JDK),
    ORACLE("Oracle", "oracle", Vendor.ORACLE, BuildScope.BUILD_OF_OPEN_JDK),
    RED_HAT("Red Hat", "redhat", Vendor.RED_HAT, BuildScope.BUILD_OF_OPEN_JDK),
    SAP_MACHINE("SAP Machine", "sap_machine", Vendor.SAP, BuildScope.BUILD_OF_OPEN_JDK),
    SEMERU("Semeru", "semeru", Vendor.IBM, BuildScope.BUILD_OF_OPEN_JDK),
    SEMERU_CERTIFIED("Semeru certified", "semeru_certified", Vendor.IBM, BuildScope.BUILD_OF_OPEN_JDK),
    TEMURIN("Temurin", "temurin", Vendor.ECLIPSE, BuildScope.BUILD_OF_OPEN_JDK),
    TRAVA("Trava", "trava", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    UBUNTU("Ubuntu", "ubuntu", Vendor.COMMUNITY, BuildScope.BUILD_OF_OPEN_JDK),
    ZULU("Zulu", "zulu", Vendor.AZUL, BuildScope.BUILD_OF_OPEN_JDK),
    ZULU_PRIME("ZuluPrime", "zulu_prime", Vendor.AZUL, BuildScope.BUILD_OF_OPEN_JDK),
    NONE("-", "", Vendor.NONE, BuildScope.NOT_FOUND),
    NOT_FOUND("", "", Vendor.NONE, BuildScope.NOT_FOUND);


    private final String uiString;
    private final String apiString;
    private final Vendor vendor;
    private final Scope  buildScope;


    Distribution(final String uiString, final String apiString, final Vendor vendor, final Scope buildScope) {
        this.uiString   = uiString;
        this.apiString  = apiString;
        this.vendor     = vendor;
        this.buildScope = buildScope;
    }


    public final String getUiString() { return uiString; }

    public final String getApiString() { return apiString; }

    public final Vendor getVendor() { return vendor; }

    public final Scope getBuildScope() { return buildScope; }

    public static final Distribution fromText(final String text) {
        if (null == text) { return NOT_FOUND; }
        switch (text) {
            case "zulu":
            case "ZULU":
            case "Zulu":
            case "zulucore":
            case "ZULUCORE":
            case "ZuluCore":
            case "zulu_core":
            case "ZULU_CORE":
            case "Zulu_Core":
            case "zulu core":
            case "ZULU CORE":
            case "Zulu Core":
                return ZULU;
            case "zing":
            case "ZING":
            case "Zing":
            case "prime":
            case "PRIME":
            case "Prime":
            case "zuluprime":
            case "ZULUPRIME":
            case "ZuluPrime":
            case "zulu_prime":
            case "ZULU_PRIME":
            case "Zulu_Prime":
            case "zulu prime":
            case "ZULU PRIME":
            case "Zulu Prime":
                return ZULU_PRIME;
            case "aoj":
            case "AOJ":
                return AOJ;
            case "aoj_openj9":
            case "AOJ_OpenJ9":
            case "AOJ_OPENJ9":
            case "AOJ OpenJ9":
            case "AOJ OPENJ9":
            case "aoj openj9":
                return AOJ_OPENJ9;
            case "azure_zulu":
            case "AZURE_ZULU":
            case "Azure_Zulu":
            case "Azure Zulu":
            case "azure zulu":
                return AZURE_ZULU;
            case "corretto":
            case "CORRETTO":
            case "Corretto":
                return CORRETTO;
            case "dragonwell":
            case "DRAGONWELL":
            case "Dragonwell":
                return DRAGONWELL;
            case "gluon_graalvm":
            case "GLUON_GRAALVM":
            case "gluongraalvm":
            case "GLUONGRAALVM":
            case "gluon graalvm":
            case "GLUON GRAALVM":
            case "Gluon GraalVM":
            case "Gluon":
                return GLUON_GRAALVM;
            case "graalvm_ce":
            case "graalvmce":
            case "GraalVM CE":
            case "GraalVMCE":
            case "GraalVM_CE":
                return GRAALVM_CE;
            case "graalvm_ee":
            case "graalvmee":
            case "GraalVM EE":
            case "GraalVMEE":
            case "GraalVM_EE":
                return GRAALVM_EE;
            case "jetbrains":
            case "JetBrains":
            case "JETBRAINS":
                return JETBRAINS;
            case "liberica":
            case "LIBERICA":
            case "Liberica":
                return LIBERICA;
            case "liberica_native":
            case "LIBERICA_NATIVE":
            case "libericaNative":
            case "LibericaNative":
            case "liberica native":
            case "LIBERICA NATIVE":
            case "Liberica Native":
            case "Liberica NIK":
            case "liberica nik":
            case "LIBERICA NIK":
            case "liberica_nik":
            case "LIBERICA_NIK":
                return LIBERICA_NATIVE;
            case "mandrel":
            case "MANDREL":
            case "Mandrel":
                return MANDREL;
            case "microsoft":
            case "Microsoft":
            case "MICROSOFT":
            case "Microsoft OpenJDK":
            case "Microsoft Build of OpenJDK":
                return MICROSOFT;
            case "ojdk_build":
            case "OJDK_BUILD":
            case "OJDK Build":
            case "ojdk build":
            case "ojdkbuild":
            case "OJDKBuild":
                return OJDK_BUILD;
            case "openlogic":
            case "OPENLOGIC":
            case "OpenLogic":
            case "open_logic":
            case "OPEN_LOGIC":
            case "Open Logic":
            case "OPEN LOGIC":
            case "open logic":
                return OPEN_LOGIC;
            case "oracle":
            case "Oracle":
            case "ORACLE":
                return ORACLE;
            case "oracle_open_jdk":
            case "ORACLE_OPEN_JDK":
            case "oracle_openjdk":
            case "ORACLE_OPENJDK":
            case "Oracle_OpenJDK":
            case "Oracle OpenJDK":
            case "oracle openjdk":
            case "ORACLE OPENJDK":
            case "open_jdk":
            case "openjdk":
            case "OpenJDK":
            case "Open JDK":
            case "OPEN_JDK":
            case "open-jdk":
            case "OPEN-JDK":
            case "Oracle-OpenJDK":
            case "oracle-openjdk":
            case "ORACLE-OPENJDK":
            case "oracle-open-jdk":
            case "ORACLE-OPEN-JDK":
                return ORACLE_OPEN_JDK;
            case "RedHat":
            case "redhat":
            case "REDHAT":
            case "Red Hat":
            case "red hat":
            case "RED HAT":
            case "Red_Hat":
            case "red_hat":
            case "red-hat":
            case "Red-Hat":
            case "RED-HAT":
                return RED_HAT;
            case "sap_machine":
            case "sapmachine":
            case "SAPMACHINE":
            case "SAP_MACHINE":
            case "SAPMachine":
            case "SAP Machine":
            case "sap-machine":
            case "SAP-Machine":
            case "SAP-MACHINE":
                return SAP_MACHINE;
            case "semeru":
            case "Semeru":
            case "SEMERU":
                return SEMERU;
            case "semeru_certified":
            case "SEMERU_CERTIFIED":
            case "Semeru_Certified":
            case "Semeru_certified":
            case "semeru certified":
            case "SEMERU CERTIFIED":
            case "Semeru Certified":
            case "Semeru certified":
                return SEMERU_CERTIFIED;
            case "temurin":
            case "Temurin":
            case "TEMURIN":
                return TEMURIN;
            case "trava":
            case "TRAVA":
            case "Trava":
            case "trava_openjdk":
            case "TRAVA_OPENJDK":
            case "trava openjdk":
            case "TRAVA OPENJDK":
                return TRAVA;
            case "kona":
            case "KONA":
            case "Kona":
                return KONA;
            case "bisheng":
            case "BISHENG":
            case "BiSheng":
            case "bi_sheng":
            case "BI_SHENG":
            case "bi-sheng":
            case "BI-SHENG":
            case "bi sheng":
            case "Bi Sheng":
            case "BI SHENG":
                return BISHENG;
            case "debian":
            case "DEBIAN":
            case "Debian":
                return DEBIAN;
            case "ubuntu":
            case "UBUNTU":
            case "Ubuntu":
                return UBUNTU;
            default:
                return NOT_FOUND;
        }
    }
}
