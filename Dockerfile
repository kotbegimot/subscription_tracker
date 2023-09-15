FROM maven:3.8.5-openjdk-17 
COPY ./src /home/app/src
COPY ./pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/subscription_tracker-0.0.1.jar", "--spring.profiles.active=dev", "-web -webAllowOthers -tcp -tcpAllowOthers -browser"]