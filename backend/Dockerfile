#Start with a base image containing Java runtime
FROM openjdk:17

#Add the application's jar to the container
COPY target/admission-app-0.0.1-SNAPSHOT.jar /folder/app.jar

#execute the application
ENTRYPOINT ["java", "-jar", "/folder/app.jar"]