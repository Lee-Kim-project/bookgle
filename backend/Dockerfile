FROM alpine:3.20 AS build

COPY backend /backend
COPY config /config

WORKDIR /backend

# Corretto 17 설치
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
    echo "https://apk.corretto.aws/" >> /etc/apk/repositories && \
    apk update && apk add amazon-corretto-17

# JAVA_HOME 및 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=file:/config/

#RUN ["./gradlew", "build"]
# 스프링 부트 빌드 시 테스트 돌아갈때 db연결이 되어 있어야 하는데 테스트가 작동하는 시점이 db 컨테이너가 준비되는 시점보다 빨라서 테스트가 실패하는 듯
# 일단 빌드 시 테스트 안하도록 설정
RUN ["./gradlew", "build", "-x", "test"]

#--------------------------------------

# 빌드 후 jar파일만 남기고 전부 삭제 & 서버 실행
FROM alpine:3.20 AS run
WORKDIR /backend
COPY --from=build backend/build/libs/*.jar build/libs/bookgle.jar
COPY --from=build /usr/lib/jvm/java-17-amazon-corretto /usr/lib/jvm/java-17-amazon-corretto
COPY --from=build /config /config

# # 시스템 유저 생성 및 전환
RUN adduser -S bookgle
USER bookgle

# JAVA_HOME 및 PATH 설정
ENV JAVA_HOME=/usr/lib/jvm/java-17-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=file:/config/

CMD ["java", "-jar", "build/libs/bookgle.jar"]
EXPOSE 8080