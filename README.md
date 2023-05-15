## JavaFinder

### Build jar file
Execute the following command in the project folder

```./gradlew clean build```

You will find two jar files in the ```build/lib``` folder:
- ```javafinder-17.0.0.jar```
- ```javafinder-17.0.0-fat.jar```

### Build native image
To build a native image, make sure you have GraalVM installed with the native-image option

#### Shell
```native-image -cp classes:build/libs/javafinder-17.0.0-fat.jar --initialize-at-build-time=Constants -H:Name=JavaFinder eu.hansolo.javafinder.Main --no-fallback```

### Usage
Find all JDK distributions on MacOS in the folder ```/System/Volumes/Data/Library/Java/JavaVirtualMachines```

#### Shell

```./JavaFinder /System/Volumes/Data/Library/Java/JavaVirtualMachines```

#### Result:

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