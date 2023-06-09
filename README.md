## JavaFinder

JavaFinder is a little command line tool to find (hopefully) all Java installations in a given path (incl. subfolders).
This can be useful if you need to figure out what kind of distributions you have installed on your machine.
It should find builds of OpenJDK and GraalVM including the following distributions:
- AdoptOpenJDK
- AdoptOpenJDK J9
- Bi Sheng
- Corretto
- Debian
- Dragonwell
- Gluon GraalVM
- Graalvm CE
- Graalvm EE
- JetBrains
- Kona
- Liberica
- Liberica Native
- Mandrel
- Microsoft
- OJDK Build
- Open Logic
- Oracle
- Oracle OpenJDK
- RedHat
- SAP Machine
- Semeru
- Semeru Certified
- Temurin
- Trava
- Ubuntu
- Zulu
- Zulu Prime

### Download the binaries
You can download the latest binaries [here](https://github.com/HanSolo/javafinder/releases).


### Requirements to build the binary yourself
- A build of OpenJDK 17 (org greater than 17)
- GraalVM CE (to build the native image)

### Build jar file
Execute the following command in the project folder

```./gradlew clean build```

You will find the jar file in the ```build/lib``` folder:
- ```javafinder-17.0.21.jar```

<br>

### Build native image
To build a native image, make sure you have GraalVM installed with the native-image option

#### Shell
```native-image -cp classes:build/libs/javafinder-17.0.21.jar --initialize-at-build-time=Constants -H:Name=javafinder eu.hansolo.javafinder.Main --no-fallback```

<br>

### Parameters
When called without any parameter ```javafinder```, it will scan the following predefined paths:
- Windows: C:\Program Files\Java\
- Linux  : /usr/lib/jvm
- MacOS  : /System/Volumes/Data/Library/Java/JavaVirtualMachines/

#### -h
Will show some info ```javafinder -h```

#### -v
Will show the version number ```javafinder -v```

### csv
Will output the result in csv format ```javafinder csv /PATH```

### json
Will output the result in json beautified format ```javafinder json /PATH```

### Not specified
If you don't specify an output format it will use json (not beautified)  ```javafinder /PATH```

<br>

### Usage
Find all JDK distributions on MacOS in the path ```/System/Volumes/Data/Library/Java/JavaVirtualMachines```
and print in non beautified json format to the console:

Shell: ```./javafinder /System/Volumes/Data/Library/Java/JavaVirtualMachines```

<br>

Find all JDK distributions on Windows in the path ```c:\``` and print in csv format to the console:

Shell: ```javafinder csv c:\```

<br>

#### Result:
The json output also contains information about the current system

Output all distributions found in <b>non beautified</b> json format
```./javafinder /System/Volumes/Data/Library/Java/JavaVirtualMachines```

```json
{"timestamp":1685524843,"search_path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/","sysinfo":{"operating_system":"Mac OS","architecture":"ARM64","bit":"64 Bit"},"distributions":[{"vendor":"Azul","name":"Zulu","version":"20.0.1","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/zulu-20.jdk/Contents/Home/","build_scope":"OpenJDK","in_use":false,"used_by":[]},{"vendor":"Azul","name":"Zulu","version":"17.0.7","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/","build_scope":"OpenJDK","in_use":true,"used_by":["/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/bin/java -XX:MaxHeapSize=32g --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048M -Dfile.encoding=UTF-8 -Duser.country=DE -Duser.language=en -Duser.variant -cp /Users/hansolo/.gradle/wrapper/dists/gradle-8.0.2-bin/25jlreiuz6u3xu2phlpa2vv4m/gradle-8.0.2/lib/gradle-launcher-8.0.2.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 8.0.2",]},{"vendor":"Oracle","name":"Graal VM CE","version":"22.3.1","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/","build_scope":"GraalVM","in_use":true,"used_by":["/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/bin/java -XX:MaxHeapSize=32g --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048M -Dfile.encoding=UTF-8 -Duser.country=DE -Duser.language=en -Duser.variant -cp /Users/hansolo/.gradle/wrapper/dists/gradle-8.0.2-bin/25jlreiuz6u3xu2phlpa2vv4m/gradle-8.0.2/lib/gradle-launcher-8.0.2.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 8.0.2",]},{"vendor":"Azul","name":"Zulu","version":"8.0.372+7","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/jre/","build_scope":"OpenJDK","in_use":false,"used_by":[]},{"vendor":"Azul","name":"Zulu","version":"11.0.19","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/zulu-11.jdk/Contents/Home/","build_scope":"OpenJDK","in_use":false,"used_by":[]},{"vendor":"Azul","name":"Zulu","version":"21-ea+22","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/zulu-21.jdk/Contents/Home/","build_scope":"OpenJDK","in_use":false,"used_by":[]},{"vendor":"Azul","name":"Zulu","version":"8.0.372+7","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/","build_scope":"OpenJDK","in_use":false,"used_by":[]},{"vendor":"Gluon","name":"Gluon GraalVM","version":"22.1.0.1","timestamp":1685524843,"path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/gluon-graalvm/Contents/Home/","build_scope":"GraalVM","in_use":false,"used_by":[]}]}
```

Output all distributions found in <b>beautified</b> json format
```./javafinder json /System/Volumes/Data/Library/Java/JavaVirtualMachines```

```json
{
  "timestamp":1685524802,
  "search_path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/",
  "sysinfo":{
    "operating_system":"Mac OS",
    "architecture":"ARM64",
    "bit":"64 Bit"
  },
  "distributions":[
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"17.0.7",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/",
      "build_scope":"OpenJDK",
      "in_use":true,
      "used_by":[
        "/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/bin/java -XX:MaxHeapSize=32g --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048M -Dfile.encoding=UTF-8 -Duser.country=DE -Duser.language=en -Duser.variant -cp /Users/hansolo/.gradle/wrapper/dists/gradle-8.0.2-bin/25jlreiuz6u3xu2phlpa2vv4m/gradle-8.0.2/lib/gradle-launcher-8.0.2.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 8.0.2"      ]
    },
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"8.0.372+7",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/",
      "build_scope":"OpenJDK",
      "in_use":false,
      "used_by":[
      ]
    },
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"20.0.1",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/zulu-20.jdk/Contents/Home/",
      "build_scope":"OpenJDK",
      "in_use":false,
      "used_by":[
      ]
    },
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"8.0.372+7",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/jre/",
      "build_scope":"OpenJDK",
      "in_use":false,
      "used_by":[
      ]
    },
    {
      "vendor":"Gluon",
      "name":"Gluon GraalVM",
      "version":"22.1.0.1",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/gluon-graalvm/Contents/Home/",
      "build_scope":"GraalVM",
      "in_use":false,
      "used_by":[
      ]
    },
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"11.0.19",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/zulu-11.jdk/Contents/Home/",
      "build_scope":"OpenJDK",
      "in_use":false,
      "used_by":[
      ]
    },
    {
      "vendor":"Oracle",
      "name":"Graal VM CE",
      "version":"22.3.1",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/",
      "build_scope":"GraalVM",
      "in_use":true,
      "used_by":[
        "/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/bin/java -XX:MaxHeapSize=32g --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2048M -Dfile.encoding=UTF-8 -Duser.country=DE -Duser.language=en -Duser.variant -cp /Users/hansolo/.gradle/wrapper/dists/gradle-8.0.2-bin/25jlreiuz6u3xu2phlpa2vv4m/gradle-8.0.2/lib/gradle-launcher-8.0.2.jar org.gradle.launcher.daemon.bootstrap.GradleDaemon 8.0.2"      ]
    },
    {
      "vendor":"Azul",
      "name":"Zulu",
      "version":"21-ea+22",
      "timestamp":1685524802,
      "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/zulu-21.jdk/Contents/Home/",
      "build_scope":"OpenJDK",
      "in_use":false,
      "used_by":[
      ]
    }
  ]
}


```

<br>
The csv output only contains the distributions found

Output all distributions found in csv format
```./javafinder csv /System/Volumes/Data/Library/Java/JavaVirtualMachines```

```
Vendor,Distribution,Version,Timestamp,Path,Type,InUse,Timestamp
Gluon,Gluon GraalVM,22.1.0.1,2023-05-31T09:19:28.069171Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/gluon-graalvm/Contents/Home/,GraalVM,false,1685524768
Azul,Zulu,8.0.372+7,2023-05-31T09:19:28.080253Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/jre/,OpenJDK,false,1685524768
Azul,Zulu,17.0.7,2023-05-31T09:19:28.075692Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/,OpenJDK,true,1685524768
Azul,Zulu,20.0.1,2023-05-31T09:19:28.077076Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/zulu-20.jdk/Contents/Home/,OpenJDK,false,1685524768
Azul,Zulu,11.0.19,2023-05-31T09:19:28.072419Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/zulu-11.jdk/Contents/Home/,OpenJDK,false,1685524768
Azul,Zulu,21-ea+22,2023-05-31T09:19:28.078641Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/zulu-21.jdk/Contents/Home/,OpenJDK,false,1685524768
Azul,Zulu,8.0.372+7,2023-05-31T09:19:28.081645Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/,OpenJDK,false,1685524768
Oracle,Graal VM CE,22.3.1,2023-05-31T09:19:28.074189Z,/System/Volumes/Data/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/,GraalVM,true,1685524768

```