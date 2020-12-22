FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Sw

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /jeecg-boot

WORKDIR /jeecg-boot

EXPOSE 8080

ADD ./target/Sw-base-application-2.2.1.jar ./

CMD sleep 60;java -Djava.security.egd=file:/dev/./urandom -jar Sw-base-application-2.2.1.jar