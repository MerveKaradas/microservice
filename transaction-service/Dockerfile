# Java runtime imajı
FROM openjdk:17-jdk-slim   
# Çalışma dizini
WORKDIR /app                
# Build edilmiş jar dosyasını konteynere kopyala
COPY target/transaction-service-0.0.1-SNAPSHOT.jar app.jar  
# Uygulamayı başlat
ENTRYPOINT ["java","-jar","app.jar"]  
