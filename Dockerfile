FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY src ./src

RUN mkdir -p bin && javac --release 17 -d bin src/*.java

CMD ["java", "-cp", "bin", "BankHttpServer"]
