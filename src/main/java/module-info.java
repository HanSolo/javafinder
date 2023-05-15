module eu.hansolo.javafinder {
    // Base
    requires java.base;
    requires java.net.http;

    // 3rd party
    requires eu.hansolo.jdktools;
    requires info.picocli;

    opens eu.hansolo.discocli to info.picocli;

    // Exports
    exports eu.hansolo.javafinder;
}