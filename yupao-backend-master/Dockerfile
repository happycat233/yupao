FROM openjdk:8-jdk-alpine

WORKDIR /app

# 本地 mvn package 后，把 target/ 下的 jar 重命名为 app.jar 放到本目录再上传
COPY app.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

# 生产环境 profile，读取 application-prod.yaml
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=prod"]
