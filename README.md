# CourseFinder

A simple Spring Boot application to search and explore educational courses using Elasticsearch.

## 🚀 Features

- Search courses by title or description
- Filter by age, price, category, type, and start date
- Sort by price or date
- Pagination support
- Suggest course titles

## ⚙️ Setup

1️⃣ Clone the project:
```
git clone https://github.com/gtm-2022/CourseFinder
```
```
cd CourseFinder
```
Start Elasticsearch (Docker):
```
docker-compose up -d
```

 Run the app:
 ```
mvn spring-boot:run
```
 Endpoints:
 Search Courses
 ```
http://localhost:8080/api/search
```

<img width="1109" height="641" alt="image" src="https://github.com/user-attachments/assets/28b3ed8e-c562-4c26-a94a-9c0ebb4b5a93" />

💬 Suggest Titles
```
http://localhost:8080/api/search/suggest?q=phy
```

<img width="1128" height="664" alt="image" src="https://github.com/user-attachments/assets/4cda9cab-100c-472b-85ac-648a8462c75e" />

Example Combinations:
```
http://localhost:8080/api/search?q=art&minAge=8&maxAge=12
```
<img width="1108" height="651" alt="image" src="https://github.com/user-attachments/assets/88198433-5692-462c-9ab1-d550e51fd55a" />

```
http://localhost:8080/api/search?category=Science&minPrice=100&maxPrice=300&sort=priceAsc
```

<img width="1113" height="647" alt="image" src="https://github.com/user-attachments/assets/9a680f8d-1211-4d58-ba3b-841dd2592375" />

```
http://localhost:8080/api/search?type=CLUB&category=Art
```
<img width="1108" height="649" alt="image" src="https://github.com/user-attachments/assets/cc38051e-4f14-4635-b480-27be0d33e91c" />

```
http://localhost:8080/api/search?page=2
```
<img width="1113" height="669" alt="image" src="https://github.com/user-attachments/assets/a2616acd-5a3d-4abe-874d-3ae316dced90" />

