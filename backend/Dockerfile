FROM alpine:3.20
WORKDIR /backend
COPY . .

# Corretto 17 설치
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
    echo "https://apk.corretto.aws/" >> /etc/apk/repositories && \
    apk update && apk add amazon-corretto-17

# JAVA_HOME 및 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH

RUN ["./gradlew", "clean", "build"]
CMD ["java", "-jar", "build/libs/bookgle-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080