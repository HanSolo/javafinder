@ECHO OFF

rem ------ CHECKSUM FILE ------------------------------------------------------
certutil -hashfile "javafinder" SHA256 > "javafinder.sha256"