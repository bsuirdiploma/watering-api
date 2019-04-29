FROM openjdk:8-jre-alpine

WORKDIR /watering-api/

ADD target/watering-api-0.0.1-SNAPSHOT.jar watering-api.jar
ADD target/swagger.json swagger/swagger.json

ENTRYPOINT java -Drun.jvmArguments=$JAVA_OPTS -jar watering-api.jar
