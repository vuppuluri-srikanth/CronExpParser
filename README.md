# CronExpParser

A Parser for Standard Cron Expressions in Java

## For Consumers

### Requirements
1. JRE 20 or above

### As Library
Add below to your pom.xml
```xml
    <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>CronParser</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```

#### Example:

```java
import org.example.cron.CronParser;

CronParser cronParser = new CronParser();
cronParser.parseExpression("*/15 0 1,15 * 1-5 /usr/bin/find");
```

### As Utility
java -jar .\CronExpParser-1.0.jar "*/15 0 1,15 * 1-5 /usr/bin/find"

# For Developers

### Java Environment

- Java SDK v20 or above
- Maven 3
- Junit 5

### Setup for Development

- Install JDK v20 or above
- Install Maven 3
- Install Intellij Idea
    - Add Plugin: Lombok
    - Enable Annotation Processors in Idea. Go to Settings. Search for 'Annotation Processors'. In the Right pane, Select the checkbox 'Enable Annotation Processing'.
- Open pom.xml in Root folder in Intellij Idea and Open as Project (Not Open as File)

