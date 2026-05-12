# Frontend

Bu klasor, `backend/` altindaki Spring Boot API icin hazirlanmis bagimsiz yonetim panelidir.

## Calistirma

1. Backend'i ayri terminalde calistir:

```bash
cd backend
mvn spring-boot:run
```

Windows icin:

```powershell
cd backend
mvn spring-boot:run
```

2. Frontend'i basit bir statik server ile ac:

```bash
cd frontend
python -m http.server 5500
```

3. Tarayicida su adrese git:

```text
http://localhost:5500
```

## API Baglantisi

Panel varsayilan olarak `http://localhost:8080/api/v1` adresine baglanir.
Istersen sol taraftaki `API Base URL` alanindan bunu degistirebilirsin.
