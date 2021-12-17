#
# Package stage
#
FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY . /app 
RUN mvn install 

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=0 /app /app 
EXPOSE 14444

CMD ["java" ,"-jar","/app/target/NinjaServer.jar"] 
