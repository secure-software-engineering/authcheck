# authcheck
Analysis for access-control vulnerabilities in Java Spring Security applications. 

Copyright (c) 2019 Secure Software Engineering Group at Paderborn University and Fraunhofer IEM
* http://www.hni.uni-paderborn.de/swt
* https://www.iem.fraunhofer.de/

## contributors
* Tobias Petrasch
* Goran Piskachev 
* Abdul Rehman Tareen
* Johannes Späth
* Eric Bodden

Contact: Goran Piskachev (goran.piskachev@iem.fraunhofer.de)

# using authcheck in MacOS or Linux 

First, compile the checker's Java code, use Maven to create the Jar file
```$ mvn package```

There are two ready-made configuration files *configuration.json* and *input.json* in *SootAnalysis* directory, which are used for demo analysis of the example application existing in the source. Before running the authcheck on MacOS or Linux, these configuration files needs to be adapted to your settings. The attributes *jceJarPath* and *rtJarPath* must be adapted to the Java Home path. 

After that, the following command can used to execute authcheck (in the contex of *SootAnalysis* directory):
```$ java -cp target/Soot-Analysis-1.0-SNAPSHOT-jar-with-dependencies.jar de.fraunhofer.iem.authchecker.Checker configuration.json```

After the running the command successfully, the analysis will create the report.html in the *report* directory, which will contain details and suggestions about the analysis.
