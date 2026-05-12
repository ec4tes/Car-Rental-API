# Car Rent Backend

Bu klasor arac kiralama uygulamasinin Spring Boot backend tarafini icerir.

## Icerik

- REST API controller'lari
- Service ve repository katmanlari
- PostgreSQL baglantisi
- Swagger dokumantasyonu

## Calistirma

Windows:

```powershell
mvn spring-boot:run
```

MacOS veya Linux:

```bash
mvn spring-boot:run
```

## Swagger

```text
http://localhost:8080/swagger-ui.html
```

## Not

Frontend artik repo kokundeki ayri `frontend/` klasorunde bulunuyor. Backend tarafinda `localhost:3000`, `4173`, `5500` ve `8081` gibi yaygin gelistirme origin'leri icin CORS izni tanimlandi.
