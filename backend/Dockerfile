FROM alpine:3.20 AS build
WORKDIR /backend

# Corretto 17 설치
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
    echo "https://apk.corretto.aws/" >> /etc/apk/repositories && \
    apk update && apk add amazon-corretto-17

# JAVA_HOME 및 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH

COPY backend .
COPY config/application.yml src/main/resources/application.yml
RUN ["./gradlew", "clean", "build"]

#--------------------------------------

# 빌드 후 jar파일만 남기고 전부 삭제 & 서버 실행
FROM alpine:3.20 AS run
WORKDIR /backend
COPY --from=build backend/build/libs/*.jar build/libs/bookgle.jar
COPY --from=build /usr/lib/jvm/java-17-amazon-corretto /usr/lib/jvm/java-17-amazon-corretto

# # 시스템 유저 생성 및 전환
RUN adduser -S bookgle
USER bookgle

# JAVA_HOME 및 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH

CMD ["java", "-jar", "build/libs/bookgle.jar"]
EXPOSE 8080