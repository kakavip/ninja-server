#
# Package stage
#
FROM openjdk:11-jre-slim
WORKDIR /usr/local/app
ADD . .
EXPOSE 14444
ENTRYPOINT ["java","-jar","/usr/local/app/target/NinjaServer.jar"]
