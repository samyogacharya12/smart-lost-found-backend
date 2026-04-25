# 📦Smart Campus Lost & Found Management System – Backend

## 📖 Overview

The **Campus Lost & Found Management System (Backend)** is a Spring Boot-based RESTful API designed to manage lost and found items within a campus. It allows users to report lost items, submit found items, and track item status efficiently.

---

## 🚀 Features

* 🔐 User authentication & authorization (JWT-based)
* 📦 Report lost items
* 📍 Location-based filtering
* 📧 Email notifications
* 📍 Submit found items
* 🔎 Search and filter items
* 🔄 Update item status
* ⚡ Lightweight data access using JDBC
*    notifications   # 🔔 stores user alerts
*    📸 Image upload for items
*    
* 🧩 Clean layered architecture (Controller → Service → DAO)

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Database:** MySQL
* **Data Access:** JDBC (Spring JDBC / JdbcTemplate)
* **Security:** Spring Security + JWT
* **Build Tool:** Gradle
* **API Testing:** Postman

---

## 📂 Project Structure

```id="3r9x2k"
src/
 ├── controller/     # REST Controllers
 ├── service/        # Business logic
 ├── dao/            # JDBC data access layer
 ├── model/          # Entity classes
 ├── dto/            # Data Transfer Objects
 └── config/         # Security & DB configuration
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the repository

```bash id="b3w9ak"
git clone https://github.com/your-username/campus-lost-found-backend.git
cd campus-lost-found-backend
```

### 2️⃣ Configure Database

Update `application.properties`:

```properties id="7w2c4z"
spring.datasource.url=jdbc:mysql://localhost:3306/lost_found_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

### 3️⃣ Create Database

```sql id="x92k1d"
CREATE DATABASE lost_found_db;
```

---

### 4️⃣ Run the Application

```bash id="p0r7fj"
./gradlew bootRun
```

---

## 🔗 API Endpoints (Sample)

| Method | Endpoint           | Description        |
| ------ | ------------------ | ------------------ |
| POST   | /api/auth/register | Register user 
| POST   | /api/auth/login    | Login user         |
| POST   | /api/items/lost    | Report lost item   |
| POST   | /api/items/found   | Report found item  |
| GET    | /api/items         | Get all items      |
| PUT    | /api/items/{id}    | Update item status |
| GET    | /api/notifications | Get user notifications |
| PUT    | /api/notification/{id}/read  | Mark notifications as read   | 


---

## 🔐 Authentication

* Uses JWT Token
* Add token in header:

```id="c8s1kd"
Authorization: Bearer <your_token>
```

---

## 🧪 Testing

Use Postman or any API client to test endpoints.

---

## 📌 Future Improvements


* 📊 Admin dashboard

---

## 👨‍💻 Author

---

## 📄 License

This project is open-source under the MIT License.
