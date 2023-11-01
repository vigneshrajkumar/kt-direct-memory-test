FROM openjdk:11-jre-slim

WORKDIR /
COPY "build/libs/direct-mem-exp-1.0.jar" .
ENTRYPOINT ["java", "-jar", "-XX:MaxRAMPercentage=50.0", "-XX:+DisableExplicitGC", "-DBOCK_SIZE=1000", "-DWAIT_DURATION=1", "direct-mem-exp-1.0.jar"]