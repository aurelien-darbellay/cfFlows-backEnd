# 🧭 cvFlows API

This project is a **Spring Boot Reactive application** that powers the back-end of **cvFlows**, a platform for creating, managing, and securely sharing CVs online. It uses **MongoDB** (with two collections) to store user data and public views.

Authentication is via **JWT** in an httpOnly cookie with a **CSRF double-submit** filter. It also supports exporting CVs to preformatted PDFs.

## 🚀 Features

- Reactive programming using **Spring WebFlux**
- User documents and public views stored in **MongoDB**
- JWT authentication via httpOnly cookie
- CSRF double-submit protection
- Preformatted PDF generation from CVs
- Interactive API documentation via **Swagger UI**
- Designed for containerized deployment via **Docker** and **Docker Compose**

---

## 🧱 Technologies & Dependencies

- Spring Boot (Reactive stack)
- Spring WebFlux
- Spring Data MongoDB Reactive
- Spring Security (JWT, CSRF)
- Thymeleaf
- openHtmlToPdf
- JUnit 5 / Reactor Test
- Java 21
- Gradle 8.14
- Cloudinary Service

---

## 📚 API Endpoints

### 🌐 Root Controller

| Method | Endpoint                | Description                             |
|--------|-------------------------|-----------------------------------------|
| POST   | `/api/register`         | Register a new user                     |
| GET    | `/api/public-views`     | Get all public views                    |
| GET    | `/api/protected/me`     | Get authenticated user details          |
| GET    | `/api/csrf`             | Get CSRF token                          |
| GET    | `/api/config`           | Get application configuration           |

---

### 👤 User Controller

| Method | Endpoint                                           | Description                                  |
|--------|----------------------------------------------------|----------------------------------------------|
| POST   | `/api/protected/users`                             | Update user profile                          |
| POST   | `/api/protected/users/public-view/{id}`            | Create new public view from a document       |
| POST   | `/api/protected/users/public-view/delete/{id}`     | Delete a public view by ID                   |
| POST   | `/api/protected/users/pdf`                         | Export a user's CV as a preformatted PDF     |
| POST   | `/api/protected/users/delete`                      | Delete user account                          |
| GET    | `/api/protected/users/public-view`                 | Get all user's public views                  |
| GET    | `/api/protected/users/dashboard`                   | Get user's dashboard data                    |

---

### 📄 Document Controller

| Method | Endpoint                                          | Description                       |
|--------|---------------------------------------------------|-----------------------------------|
| POST   | `/api/protected/users/doc`                        | Create a new document             |
| GET    | `/api/protected/users/doc/{docId}`                | Get a document by ID              |
| POST   | `/api/protected/users/doc/{docId}`                | Update a document                 |
| POST   | `/api/protected/users/doc/{docId}/delete`         | Delete a document                 |

---

### 📝 Entry Controller

| Method | Endpoint                                               | Description                      |
|--------|--------------------------------------------------------|----------------------------------|
| POST   | `/api/protected/users/doc/{docId}/entry/update`        | Update an entry in a document    |
| POST   | `/api/protected/users/doc/{docId}/entry/delete`        | Delete an entry from a document  |
| POST   | `/api/protected/users/doc/{docId}/entry/add`           | Add a new entry to a document    |

---

### ☁️ Cloud Storage Controller

| Method | Endpoint                                            | Description                         |
|--------|-----------------------------------------------------|-------------------------------------|
| POST   | `/api/protected/users/cloud-storage/signature`      | Generate a signed upload URL        |
| POST   | `/api/protected/users/cloud-storage/delete`         | Delete a file from cloud storage    |

---

### 🔐 Admin Controller

| Method | Endpoint                    | Description                      |
|--------|-----------------------------|----------------------------------|
| GET    | `/api/protected/admin`      | Access admin-only information    |

---

## 🗂️ MongoDB Collections

- **user**: Stores user accounts and their CV documents
- **public-view**: Stores public, shareable screenshots of CVs accessible without authentication

---

## 📦 Deployment on Render

**Backend Render URL**:  
https://interactive-cv-backend-latest.onrender.com/

---

## 📖 API Documentation

Swagger UI is available at:  
https://interactive-cv-backend-latest.onrender.com/webjars/swagger-ui/index.html

If you serve locally with Docker Compose:  
https://localhost:8080/webjars/swagger-ui/index.html

---

## 🔗 GitHub

You can find the source code for this project on GitHub:  
https://github.com/aurelien-darbellay/cvFlows-backEnd

Docker image hosted in GitHub Packages:  
https://ghcr.io/aurelien-darbellay/interactive-cv-backend:latest

---

## 🐳 Run with Docker

1. Clone the repository:

   ```bash
   git clone https://github.com/aurelien-darbellay/s05t02_2.git

## 📥 ➡️ 🛠️ ➡️ ✅ GitHub/Render workflow
GitHub Actions workflow for automated deployment to Render:
https://github.com/aurelien-darbellay/s05t02_2/blob/main/.github/workflows/deploy-to-render.yml
