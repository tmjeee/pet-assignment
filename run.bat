java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -Dhttps.protocols=TLSv1.2,TLSv1.1,TLSv1 -jar target/pet-assignment-0.0.1-SNAPSHOT.jar
