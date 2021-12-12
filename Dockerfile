#
# Package stage
#
FROM openjdk:11-jre-slim
WORKDIR /app
ADD . .
EXPOSE 14444
ENTRYPOINT ["java","-jar","/app/target/NinjaServer.jar"]
