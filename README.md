# client


## to build

> You need to have Java Installed 
> 
Go inside the client project
```
./gradlew build
```

```shell
java -classpath build/libs/client-1.0-SNAPSHOT.jar  org.arcticwolf.Main
```

## config File Location
```shell
client/src/main/config/config.properties
```

### config properties default
```shell
directory.path=src/main/resources,src/main/resources1,src/main/resources2
key.filter=server(.)*
server.address=127.0.0.1
server.port=8282
```
**directory.path is comma seperated directories which needed to be watched for .properties file**

**key.filter is regex of the key which needed to be filtered**

## Default Setup
> The directories src/main/resources, src/main/resources1, src/main/resources2 are already created
> with a dummy file so that I can commit folder structure in git


