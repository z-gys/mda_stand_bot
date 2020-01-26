FROM openjdk:8u151-jre-alpine

WORKDIR /opt/app

RUN apk add --update --no-cache


COPY docker/entrypoint.sh /
COPY target/*.jar .

ENTRYPOINT ["sh", "/entrypoint.sh"]