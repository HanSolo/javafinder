## JavaFinder

### Build jar file
Execute the following command in the project folder

```./gradlew clean build```

You will find two jar files in the ```build/lib``` folder:
- ```javafinder-17.0.0.jar```
- ```javafinder-17.0.0-fat.jar```

<br>

### Build native image
To build a native image, make sure you have GraalVM installed with the native-image option

#### Shell
```native-image -cp classes:build/libs/javafinder-17.0.0-fat.jar --initialize-at-build-time=Constants -H:Name=javafinder eu.hansolo.javafinder.Main --no-fallback```

<br>

### Usage
Find all JDK distributions on MacOS in the path ```/System/Volumes/Data/Library/Java/JavaVirtualMachines```
and print in json format to the console:

Shell: ```./javafinder /System/Volumes/Data/Library/Java/JavaVirtualMachines```

<br>

Find all JDK distributions on Windows in the path ```c:\\``` and print in csv format to the console:

Shell: ```javafinder csv c:\```

<br>

#### Result:
The json output also contains information about the current system

Output all distributions found in json format
```./javafinder /System/Volumes/Data/Library/Java/JavaVirtualMachines```

```json
{
   "search_path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines",
   "sysinfo":{
      "operating_system":"macos",
      "architecture":"arm64",
      "bit":"64"
   },
   "distributions":[
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"17.0.7",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"8.0.372+7",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/jre/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"8.0.372+7",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"11.0.19",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/zulu-11.jdk/Contents/Home/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"20.0.1",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/zulu-20.jdk/Contents/Home/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Oracle",
         "name":"Graal VM CE",
         "version":"22.3.1",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/",
         "build_scope":"build_of_graalvm"
      },
      {
         "vendor":"Azul",
         "name":"Zulu",
         "version":"21-ea+21",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/zulu-21.jdk/Contents/Home/",
         "build_scope":"build_of_openjdk"
      },
      {
         "vendor":"Gluon",
         "name":"Gluon GraalVM",
         "version":"22.1.0.1",
         "path":"/System/Volumes/Data/Library/Java/JavaVirtualMachines/gluon-graalvm/Contents/Home/",
         "build_scope":"build_of_graalvm"
      }
   ]
}
```

<br>
The csv output only contains the distributions found

Output all distributions found in csv format
```./javafinder csv /System/Volumes/Data/Library/Java/JavaVirtualMachines```

```
vendor,distribution,version,location,buildscope
Azul,Zulu,21-ea+21,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/zulu-21.jdk/Contents/Home/,Build of OpenJDK
Oracle,Graal VM CE,22.3.1,/System/Volumes/Data/Library/Java/JavaVirtualMachines/graalvm-ce-java17-22.3.1/Contents/Home/,Build of GraalVM
Azul,Zulu,20.0.1,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/zulu-20.jdk/Contents/Home/,Build of OpenJDK
Azul,Zulu,11.0.19,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/zulu-11.jdk/Contents/Home/,Build of OpenJDK
Azul,Zulu,8.0.372+7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/jre/,Build of OpenJDK
Azul,Zulu,8.0.372+7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/,Build of OpenJDK
Azul,Zulu,20.0.1,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-20.jdk/,Build of OpenJDK
Azul,Zulu,21-ea+21,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-21.jdk/,Build of OpenJDK
Gluon,Gluon GraalVM,22.1.0.1,/System/Volumes/Data/Library/Java/JavaVirtualMachines/gluon-graalvm/Contents/Home/,Build of GraalVM
Azul,Zulu,8.0.372+7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/,Build of OpenJDK
Azul,Zulu,17.0.7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/,Build of OpenJDK
Azul,Zulu,11.0.19,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-11.jdk/,Build of OpenJDK
Azul,Zulu,17.0.7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-17.jdk/zulu-17.jdk/Contents/Home/,Build of OpenJDK
Azul,Zulu,8.0.372+7,/System/Volumes/Data/Library/Java/JavaVirtualMachines/zulu-8.jdk/zulu-8.jdk/Contents/Home/jre/,Build of OpenJDK

```