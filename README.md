# authcheck
Analysis for access-control vulnerabilities in Java Spring Security applications. 

Copyright (c) 2019 Secure Software Engineering Group at Paderborn University and Fraunhofer IEM
* http://www.hni.uni-paderborn.de/swt
* https://www.iem.fraunhofer.de/

## contributors
* Tobias Petrasch
* Goran Piskachev 
* Johannes Späth
* Eric Bodden

Contact: Goran Piskachev (goran.piskachev@iem.fraunhofer.de)

# using authcheck

Two different procedures can be used to run the authcheck: a system independent variant with Docker, or a direct execution of the analysis on the system. To compile the checker's Java code first, use Maven to create Jar file
```$ mvn package```

Using Docker, authcheck can be run independently of the system. There are three ready-made configurations that can be run using different commands. These include the analysis of the application example and the test scenarios. 

For execution, Docker-Compose has already been used to configure the Docker containers, which allows the checker to run with one command. The Docker container was defined generically, so that the program to be analyzed, the configurations and the templates must be defined as volumes. These are mounted in the container at runtime. Docker-Compose allows the configuration of the volumes in the file docker-compose.yml.

To analyze the demo example, create the Docker image and run authcheck. Use the command:
```$ docker-compose up -d --build checker ```

With the following command the log files can be viewed:
```$ docker-compose logs -f checker```

The generated HTML report (report.html) is stored in the directory ```../docker/output/default```.

Analyze the test cases:
Use the following command:
```$ docker-compose up -d --build checker-testcases```

With the following command the log files can be viewed:
```$ docker-compose logs -f checker-testcases```

The generated HTML report (report.html) is stored in the directory ```../docker/output/testcases```.

## MacOS and Linux
To run authcheck on MacOS or Linux, configuration file has to be adapted. There are example configurations in the folder configuration. Among them are configuration default.json and configuration testcases.json. 
The attributes jceJarPath and rtJarPath must be adapted to the Java Home path. The following command can then be executed to execute authcheck:
```$ java -jar target/Checker-1.0-SNAPSHOT-jar-withdependencies.jar configuration/configuration default.json```

The configuration parameter must be adjusted accordingly. The generated HTML report (report.html) can be found in the report directory.



