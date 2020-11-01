FROM alpine:3.12 as builder

RUN apk add openjdk8

COPY . /app

WORKDIR /app

RUN ./gradlew clean --no-daemon
RUN ./gradlew build --no-daemon

FROM alpine:latest

RUN apk add openjdk8

COPY --from=builder /app/build/libs/bot.jar /app/bot.jar

EXPOSE 80

CMD java - jar /app/bot.jar