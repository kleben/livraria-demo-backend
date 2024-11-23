FROM amazoncorretto:17
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} livraria-1-0.jar
ENTRYPOINT ["java","-jar","/livraria-1-0.jar"]