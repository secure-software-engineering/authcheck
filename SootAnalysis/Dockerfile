FROM openjdk:8
LABEL Maintainer="Tobias Petrasch <tobias.petrasch@fynedigital.com>" \
      Description="Checker for CWEs"

RUN mkdir -p /opt/checker

COPY ./target/Soot-Analysis-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/checker/checker.jar

WORKDIR /opt/checker

CMD ["java", "-jar", "checker.jar", "configuration.json"]
