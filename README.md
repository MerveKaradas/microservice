# Dijital Cüzdan / Bankacılık Mikroservisleri

> Bu proje geliştirme aşamasındadır ve mikroservis mimarisinde bir dijital cüzdan / bankacılık sistemi oluşturmayı amaçlamaktadır.

---

## 1. Proje Hakkında
- Mikroservis tabanlı bir dijital cüzdan / bankacılık uygulaması.
- Ana işlevler: kullanıcı yönetimi, hesap yönetimi, bakiye ve para transferleri, döviz kuru yönetimi, bildirimler.
- Geliştirme aşamasında temel servis iskeletleri oluşturuluyor.

---

## 2. Kullanılacak Teknolojiler

| Katman | Teknoloji / Araç | Açıklama |
|--------|-----------------|----------|
| Backend | Java Spring Boot | Mikroservisler için temel framework |
| Service Discovery | Eureka | Servislerin birbirini bulması ve yönetimi |
| Config Management | Spring Cloud Config | Merkezi konfigürasyon yönetimi |
| Security | JWT, Spring Security | Kullanıcı kimlik doğrulama ve yetkilendirme |
| Database | PostgreSQL | Her servis için ayrı veritabanı (DB-per-service) |
| Cache | Redis | Döviz kuru, session veya sık kullanılan veriler için |
| Event / Messaging | Kafka veya RabbitMQ | Servisler arası asenkron iletişim ve event publish/subscribe |
| API Gateway | Spring Cloud Gateway / NGINX | JWT doğrulama, routing, rate limiting |
| Container | Docker & Docker Compose | Servisleri izole ve taşınabilir çalıştırmak için |
| Scheduler | Spring Scheduler | Döviz kuru güncellemeleri ve periyodik görevler |
| Testing | JUnit, Testcontainers | Unit ve integration testler |
| CI/CD | GitHub Actions / GitLab CI | Otomatik build, test ve deploy pipeline |

---

## 3. Mikroservisler ve Görevleri

- **Auth / Identity Service:** JWT token üretimi, kullanıcı doğrulama, roller ve izin yönetimi  
- **User Service:** Kullanıcı profili CRUD, KYC bilgileri  
- **Account Service:** Hesap açma, bakiye yönetimi, concurrency kontrol  
- **Transaction Service:** Para transferleri, işlem durumu yönetimi, idempotency, Saga / Outbox pattern  
- **FX / Rates Service:** Döviz kuru API entegrasyonu, Redis cache, scheduler  
- **Notification Service:** Email / SMS / push bildirimleri  
- **Ledger / Audit Service:** Immutable transaction log, reconciliation  

> Tüm servisler Eureka üzerinden kayıtlıdır ve service discovery ile birbirlerini bulabilir.  

---

## 4. Yapılması Planlanan Görevler

- **Transaction Service:** Ayrılması ve Outbox + Saga pattern ile asenkron ve tutarlı transaction yönetimi  
- **FX / Rates Service:** Redis cache ve scheduler ile döviz kuru güncellemelerinin entegrasyonu  
- **Notification Service:** Transaction ve sistem bildirimlerinin eklenmesi  
- **Account Service:** Concurrency kontrol ve atomic SQL işlemlerinin iyileştirilmesi  
- **Docker Compose:** Tüm servisler için geliştirme ortamının düzenlenmesi  
- **CI/CD Pipeline:** Build → Test → Deploy Staging adımlarının kurulması  
- **Prod-ready Altyapı:** Kubernetes deployment, Vault secrets management, HPA, cluster autoscaling  
- **Observability:** Prometheus + Grafana, Jaeger tracing, merkezi log yönetimi  
- **Opsiyonel Servisler:** Card / PSP entegrasyonu, Fraud / AML servisleri  


## 5. Testler ve CI/CD

- Unit testler: JUnit

- Integration testler: Testcontainers

- Contract testler: Provider/Consumer

- CI/CD pipeline: Build → Test → Deploy Staging
