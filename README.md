# DubTrack4J
A Java API for Dubtrack.

## Warning: I'm only updating this API to suit my own needs. This means that I'm not caring about any feature that I'm not using in my own projects, and it certainly means that I'm not going to provide any support. Use at your own risk.

[![Build Status](https://travis-ci.org/sponges/DubTrack4J.svg?branch=master)](https://travis-ci.org/Sponges/DubTrack4J)
[![JitPack](https://jitpack.io/v/sponges/dubtrack4j.svg)](https://jitpack.io/#sponges/dubtrack4j)

### Maven
This project uses JitPack as a maven repository (for now).

Respository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Dependency:
```xml
<dependency>
    <groupId>com.github.sponges</groupId>
    <artifactId>dubtrack4j</artifactId>
    <version>1.06-SNAPSHOT</version>
</dependency>
```

### Usage
##### Creating your DubtrackAPI Builder instance:
```java
DubtrackBuilder builder = new DubtrackBuilder(username, password);
// here you can set the properties with the builder instance
DubtrackAPI dubtrack = builder.buildAndLogin();
```

##### Joining a room:
This method takes a String room name, which is found at the end of the join url. http://dubtrack.fm/join/RoomName
```java
Room room = dubtrack.joinRoom(roomName);
```

##### Listening for events:
The `EventBus` class uses consumers to handle events.
```java
dubtrack.getEventBus().register(UserChatEvent.class, event -> {
    System.out.println("Got message: " + event.getMessage().getContent());
});
```
See the [Event package](https://github.com/Sponges/DubTrack4J/tree/master/src/main/java/io/sponges/dubtrack4j/event) to see all the available events.

##### Sending a message
```java
room.sendMessage("This is a message!");
```

##### Other stuff
Check out the javadocs @ placeholder.exe
