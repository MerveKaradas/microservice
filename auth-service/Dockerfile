# Java runtime imajı
FROM openjdk:17-jdk-slim   
# Çalışma dizini
WORKDIR /app                
# Build edilmiş jar dosyasını konteynere kopyala
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar  
# Uygulamayı başlat
ENTRYPOINT ["java","-jar","app.jar"]  

# Docker Compose’daki build: ./auth-service kısmı, uygulamayı Docker imajı haline getirmek için Dockerfile’a ihtiyaç duyar.
# Dockerfile yoksa Compose build sırasında neyi nasıl paketleyeceğini bilmez ve hata verir.
# Bu nedenle, her mikroservis dizininde bir Dockerfile bulunmalıdır.
# Eğer dockerfile kullanmak istenmezse : 
# Docker Compose’da image: kullanıp önceden build edilmiş .jar imajı çekilebilir.
# Ama her build sonrası .jar değiştiğinde imajı yeniden oluşturmak gerekir. Dockerfile ile build süreci otomatikleşir.