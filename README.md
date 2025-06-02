# 🔗 URL Shortener

A minimal and efficient URL shortening service built with Spring Boot and Cassandra.

---

## ✨ Features

- 🔹 Shorten long URLs to compact, easy-to-share links
- 🔸 Redirect short URLs to their original long form

---

## 🧠 How It Works

- Each shortened URL is a **7-character string**
- Characters are selected from:
    - `A–Z` (uppercase)
    - `a–z` (lowercase)
    - `0–9` (digits)
- This results in over **3.5 trillion** possible combinations (62⁷), ensuring high uniqueness and
  scalability

---

## 🔌 API Overview

### 🎯 POST `/api/url/shorten`

Creates a short URL from a long one.

**Request Body (JSON):**

```json
{
  "url": "https://example.com/"
}
```

**Response Body 200 OK (JSON):**

```json
{
  "shortUrl": "http://localhost:8080/Ab3dXyZ",
  "code": "Ab3dXyZ"
}
```

**Response Body 404 Not Found (JSON):**

```json
{
  "message": "Invalid URL format."
}
```

### 🔁 GET `/api/url/{shortCode}`

Redirects to the original long URL.

```
GET http://localhost:8080/Ab3dXyZ
```

**Response:**

- 302 Found (redirects to the original URL)
- 404 Not Found if the code doesn’t exist

📌 Note: The short URL domain is configurable via the **app.short-domain** property in
application.properties.

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.0**
- **Apache Cassandra (via Docker)**
- **JUnit 5, Mockito, Testcontainers**

---

## ⚙️ Prerequisites

- ✅ [Docker](https://www.docker.com/) installed and running
- ✅ Java 21 (ensure it's set in your environment)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/url-shortener.git
cd url-shortener
```

### 2. Start Cassandra via Docker

```bash
docker-compose up -d
```

This will spin up Cassandra in the container.

### 3. Initialize the Database

Once Cassandra is running:

```bash
docker exec -it <cassandra-container-name> cqlsh
```

Then run the init.cql commands from the resources folder.

### 4. Run the application

```bash
./gradlew bootRun
```

The app will start on http://localhost:8080

## 🧪 Running Tests

```bash
./gradlew test
```

Includes unit tests, integration tests with Testcontainers for Cassandra.

---

## 📁 Project Structure

- **/src/main/java/com/antonina/urlshortener** — application code

- **/src/main/resources/init.cql** — Cassandra init script

- **build.gradle.kts** — Gradle build config

---

## 🤝 Contributing

Contributions, ideas and feedback are welcome!

1. Fork the repo
2. Create a new branch: git checkout -b feature/some-feature
3. Commit your changes: git commit -m 'Add some feature'
4. Push to the branch: git push origin feature/some-feature
5. Open a pull request

## ⭐ License

This project is licensed under the MIT License.

## ❤️ Support

Thanks for checking out this project! If you find it helpful or interesting, here are a few ways you
can support it:

- ⭐ Give it a star on GitHub — it really helps with visibility.
- 🗣️ Share it with others who might find it useful.
- 🐛 Found a bug? Have an
  idea? [Open an issue](https://github.com/antoshka412/url-shortener/issues) — I'd love to hear from
  you.
- 🤝 Want to contribute? Check out the [Contributing](#contributing) section.
- 💌 You can also reach out on [LinkedIn](https://www.linkedin.com/in/antonina-poliachenko-b834868b/)

I built this project as a learning tool and a practical utility — every bit of feedback or
encouragement keeps it going!
