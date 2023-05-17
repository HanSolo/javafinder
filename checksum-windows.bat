@ECHO OFF

rem ------ CHECKSUM FILE ------------------------------------------------------
certutil -hashfile "javafinder.exe" SHA256 > "javafinder.exe.sha256"