# syntax = docker/dockerfile:1.2

# Stage 1: Build the application using Clojure
FROM clojure:openjdk-17 AS build

# Install Clojure CLI tools (if not already installed)
RUN apt-get update && apt-get install -y curl \
    && curl -O https://download.clojure.org/install/linux-install-1.11.1.1135.sh \
    && chmod +x linux-install-1.11.1.1135.sh \
    && ./linux-install-1.11.1.1135.sh \
    && rm linux-install-1.11.1.1135.sh

WORKDIR /
COPY . /

# Run the build
RUN clj -Sforce -T:build all

# Stage 2: Final image with the built app
FROM azul/zulu-openjdk-alpine:17

COPY --from=build /target/trukun-standalone.jar /trukun/trukun-standalone.jar

EXPOSE $PORT

ENTRYPOINT exec java $JAVA_OPTS -jar /trukun/trukun-standalone.jar
