# Car Rent Workspace

Bu repo artik iki net parcaya ayrildi:

- `backend/`: Spring Boot REST API
- `frontend/`: API'ye baglanan bagimsiz yonetim paneli

## Klasor Yapisi

```text
car-rent/
|-- backend/
|-- frontend/
|-- .gitignore
|-- .gitattributes
```

## Backend'i Calistirma

```powershell
cd backend
mvn spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## Frontend'i Calistirma

Statik bir server yeterli:

```powershell
cd frontend
python -m http.server 5500
```

Ardindan:

```text
http://localhost:5500
```

Frontend varsayilan olarak `http://localhost:8080/api/v1` adresine baglanir.

Not:

`mvnw.cmd` bu Codex kabugunda dogrulanamadi; bu nedenle calisan komut olarak sistemde kurulu `mvn` kullanildi.
