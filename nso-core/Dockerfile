#
# Package stage
#
FROM maven:3.5-jdk-8-alpine as target
WORKDIR /app

# copy the Project Object Model file
COPY ./repo ./repo
COPY ./pom.xml ./pom.xml

# fetch all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
# NOTE: my-project-* should be replaced with the proper prefix
RUN mvn package && cp target/NinjaServer.jar NinjaServer.jar


FROM openjdk:8-jre-alpine
WORKDIR /app

# copy config files
COPY ./black_list_ips.txt ./log/black_list_ips.txt
COPY ./ninja.conf ./ninja.conf
COPY ./application.properties ./application.properties

# copy build file
COPY --from=target /app/NinjaServer.jar /app/NinjaServer.jar

EXPOSE 14444

CMD ["java" ,"-jar","/app/NinjaServer.jar"] 
