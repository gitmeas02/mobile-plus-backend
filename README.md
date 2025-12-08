# 🚀 Mobile Plus Backend

A comprehensive Spring Boot backend application with advanced authentication features including OTP verification via Email/Telegram and Social Login (Google, GitHub, Telegram).

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## ✨ Features

### 🔐 Authentication
- ✅ **Traditional Login:** Email/Username + Password with OTP verification
- ✅ **Social Login:** Google, GitHub, Telegram OAuth2
- ✅ **OTP Delivery:** Email or Telegram messaging
- ✅ **JWT Tokens:** Secure token-based authentication
- ✅ **User Registration:** With email/Telegram verification

### 📧 OTP System
- ✅ 6-digit OTP codes
- ✅ 5-minute validity period
- ✅ Professional email templates
- ✅ Telegram bot integration
- ✅ Resend OTP functionality

### 🔒 Security
- ✅ BCrypt password encryption
- ✅ JWT token authentication
- ✅ OAuth2 social login
- ✅ Session management

### 🛠️ CI/CD
- ✅ GitHub Actions workflow
- ✅ Jenkins pipeline
- ✅ Docker containerization
- ✅ Automated testing

---

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/gitmeas02/mobile-plus-backend.git
cd mobile-plus-backend
```

### 2. Configure Environment
Update the `.env` file with your credentials (see SETUP_GUIDE.md for details)

### 3. Run Application
```bash
./gradlew bootRun
```

Application starts at: `http://localhost:8080`

---

## 📚 Documentation

- **[Setup Guide](SETUP_GUIDE.md)** - Complete installation and configuration guide
- **[API Documentation](API_DOCUMENTATION.md)** - All endpoints and usage examples

---

## 🔑 Main API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user with OTP |
| POST | `/auth/login` | Login with email/password |
| POST | `/auth/verify-otp` | Verify OTP code |
| POST | `/auth/resend-otp` | Resend OTP |
| GET | `/oauth2/authorization/google` | Login with Google |
| GET | `/oauth2/authorization/github` | Login with GitHub |

---

## 🧪 Quick Test

```bash
# Register a user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test123456",
    "otpDeliveryMethod": "email"
  }'
```

---

## 🐳 Docker Support

```bash
# Start all services (MySQL + Backend)
docker-compose up -d

# View logs
docker-compose logs -f backend

# Stop services
docker-compose down
```

---

## 🏗️ Tech Stack

- **Framework:** Spring Boot 3.5.7
- **Language:** Java 21
- **Database:** MySQL 8.0
- **Security:** Spring Security + JWT
- **Email:** JavaMailSender (SMTP)
- **Messaging:** Telegram Bot API
- **OAuth2:** Spring OAuth2 Client
- **Build Tool:** Gradle 8.5
- **Containerization:** Docker & Docker Compose

---

## 📊 Project Structure

```
mobile_plus_backend/
├── src/main/java/com/example/mobile/
│   ├── auth/              # Authentication controllers & services
│   ├── user/              # User management
│   ├── config/            # Security & application configuration
│   └── MobileApplication.java
├── .github/workflows/     # GitHub Actions CI/CD
├── docker-compose.yml     # Docker services configuration
├── Dockerfile             # Application container
├── Jenkinsfile           # Jenkins pipeline
└── .env                  # Environment variables
```

---

## 🔧 Configuration Required

1. **Email (Gmail):** App password for sending OTP emails
2. **Telegram Bot:** Bot token from @BotFather
3. **Google OAuth2:** Client ID and Secret
4. **GitHub OAuth2:** Client ID and Secret
5. **Database:** MySQL credentials
6. **JWT:** Secret key for token signing

See [SETUP_GUIDE.md](SETUP_GUIDE.md) for detailed configuration instructions.

---

## 🔄 CI/CD Pipelines

### GitHub Actions
- Automatic build and test on push
- Docker image creation and push
- Deployment to production

### Jenkins
- Complete build pipeline
- Automated testing
- Code quality checks
- Docker deployment
- Health monitoring

---

## 📝 License

This project is licensed under the MIT License.

---

## 📞 Support

- **Documentation:** [SETUP_GUIDE.md](SETUP_GUIDE.md) | [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Issues:** [GitHub Issues](https://github.com/gitmeas02/mobile-plus-backend/issues)

---

Made with ❤️ by the Mobile Plus Team
