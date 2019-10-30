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

First, compile the AuthCheck's source, use the following Maven command for that in the context of *./SootAnalysis* directory.
```$ mvn package```

Then, compile demo/example application by using the following Maven command in the context of *./Spring_Examples/demo* directory.  
```$ mvn package```

There are two ready-made configuration files *configuration.json* and *input.json* in *./SootAnalysis* directory, which are used for demo analysis of the example application existing in the source. The former file contains various settings for the analysis and the latter contains the input model needed for the analysis.

Before running the authcheck on MacOS or Linux, the *configuration.json* file needs to be modified according to your settings; the attributes *jceJarPath* and *rtJarPath* must include your Java Home path. 

After that, in the contex of *./SootAnalysis* directory, the following command can be used to execute authcheck:  
```$ java -cp target/Soot-Analysis-1.0-SNAPSHOT-jar-with-dependencies.jar de.fraunhofer.iem.authchecker.Checker configuration.json```

After the running the successful execution of the above command, the analysis will create the report.html in the *./report* directory, which will contain results and suggestions about the analysis.
