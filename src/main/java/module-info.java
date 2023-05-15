module eu.hansolo.javafinder {
    // Base
    requires java.base;

    // 3rd party
    requires transitive eu.hansolo.jdktools;

    // Exports
    exports eu.hansolo.javafinder;
}