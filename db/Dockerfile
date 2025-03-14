FROM mariadb:latest

# 환경 변수 설정
ENV MYSQL_ROOT_PASSWORD=test\
    MYSQL_DATABASE=mariadb\
    MYSQL_USER=root\
    MYSQL_PASSWORD=test

# 초기화 스크립트 복사
#COPY db/init.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
