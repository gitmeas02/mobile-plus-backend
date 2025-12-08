# Mobile Plus Backend - API Documentation

## 🚀 Complete Authentication System

This backend provides a comprehensive authentication system with:
- Traditional email/password login with OTP verification
- Social login (Google, GitHub, Telegram)
- OTP delivery via Email or Telegram
- JWT token-based authentication

---

## 📋 Table of Contents
- [Authentication Endpoints](#authentication-endpoints)
- [OTP Endpoints](#otp-endpoints)
- [Setup Instructions](#setup-instructions)
- [Environment Variables](#environment-variables)
- [CI/CD Pipeline](#cicd-pipeline)

---

## 🔐 Authentication Endpoints

### 1. Register New User
**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "phoneNumber": "+1234567890",
  "telegramChatId": "123456789",
  "otpDeliveryMethod": "email"
}
```

**Response:**
```json
{
  "message": "Registration successful! Please verify your OTP sent to your email",
  "requiresOtp": true,
  "email": "john@example.com"
}
```

**OTP Delivery Methods:**
- `"email"` - Send OTP via email (default)
- `"telegram"` - Send OTP via Telegram bot

---

### 2. Login with Email/Password
**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Alternative (using username):**
```json
{
  "username": "johndoe",
  "password": "SecurePass123"
}
```

**Response:**
```json
{
  "message": "OTP sent to your email. Please verify to complete login.",
  "requiresOtp": true,
  "email": "john@example.com"
}
```

---

### 3. Verify OTP
**Endpoint:** `POST /auth/verify-otp`

**Request Body:**
```json
{
  "email": "john@example.com",
  "otpCode": "123456"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "uuid-here",
  "username": "johndoe",
  "email": "john@example.com",
  "message": "Login successful!"
}
```

---

### 4. Resend OTP
**Endpoint:** `POST /auth/resend-otp`

**Request Body:**
```json
{
  "email": "john@example.com",
  "deliveryMethod": "telegram"
}
```

**Response:**
```json
{
  "message": "New OTP sent to your telegram",
  "requiresOtp": true
}
```

---

### 5. OAuth2 Social Login

#### Google Login
**Endpoint:** `GET /oauth2/authorization/google`

Redirects to Google OAuth consent screen. After successful authentication, user is redirected back with JWT token.

#### GitHub Login
**Endpoint:** `GET /oauth2/authorization/github`

Redirects to GitHub OAuth consent screen. After successful authentication, user is redirected back with JWT token.

**OAuth Callback Response:**
Redirects to: `http://localhost:3000/auth/callback?token={JWT_TOKEN}&user={USERNAME}&email={EMAIL}`

---

## 📱 OTP Endpoints

### 1. Verify OTP (Alternative)
**Endpoint:** `POST /otp/verify`

Same as `/auth/verify-otp` - provided for convenience.

### 2. Resend OTP (Alternative)
**Endpoint:** `POST /otp/resend`

Same as `/auth/resend-otp` - provided for convenience.

---

## ⚙️ Setup Instructions

### 1. Prerequisites
- Java 21 or higher
- MySQL 8.0 or higher
- Gradle 8.5 or higher
- Docker & Docker Compose (optional)

### 2. Database Setup
```sql
CREATE DATABASE mobile_plus_db;
CREATE USER 'user_mobile_plus'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON mobile_plus_db.* TO 'user_mobile_plus'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Environment Variables

Create a `.env` file in the project root:

```env
# Database Configuration
MY_SQL_HOST=localhost
MY_SQL_PORT=3306
MY_SQL_USER=user_mobile_plus
MY_SQL_PASSWORD=your_password_mobile_plus_db
MY_SQL_DB=mobile_plus_db

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here
JWT_EXPIRATION_MS=86400000

# Email Configuration (Gmail)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Telegram Bot Configuration
TELEGRAM_BOT_TOKEN=your_bot_token_from_botfather
TELEGRAM_BOT_USERNAME=your_bot_username

# OAuth2 Google
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# OAuth2 GitHub
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

### 4. Get Telegram Bot Token
1. Open Telegram and search for `@BotFather`
2. Send `/newbot` command
3. Follow instructions to create bot
4. Copy the bot token and username
5. Users can get their chat ID by:
   - Starting your bot
   - Sending `/getchatid` command

### 5. Configure Gmail App Password
1. Go to Google Account settings
2. Enable 2-Factor Authentication
3. Generate App Password for "Mail"
4. Use this password in `MAIL_PASSWORD`

### 6. Setup OAuth2 Providers

#### Google OAuth2:
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create new project or select existing
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
6. Copy Client ID and Secret

#### GitHub OAuth2:
1. Go to GitHub Settings → Developer settings → OAuth Apps
2. Create new OAuth App
3. Set Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Copy Client ID and Secret

### 7. Build and Run

#### Using Gradle:
```bash
./gradlew clean build
./gradlew bootRun
```

#### Using Docker Compose:
```bash
docker-compose up -d
```

Application will start on: `http://localhost:8080`

---

## 🧪 Testing Endpoints

### Using cURL:

**Register:**
```bash
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

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123456"
  }'
```

**Verify OTP:**
```bash
curl -X POST http://localhost:8080/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "otpCode": "123456"
  }'
```

---

## 🔄 CI/CD Pipeline

### GitHub Actions
Automatically triggered on push to `main` or `develop` branches:
- Builds application
- Runs tests
- Creates Docker image
- Pushes to Docker Hub (on main branch)
- Deploys to production (on main branch)

**Required GitHub Secrets:**
- `DOCKER_USERNAME`
- `DOCKER_PASSWORD`
- `DEPLOY_HOST`
- `DEPLOY_USER`
- `DEPLOY_SSH_KEY`

### Jenkins Pipeline
Configure Jenkins with:
- JDK 21 tool
- Docker plugin
- Credentials for Docker Hub

**Required Jenkins Credentials:**
- `docker-hub-credentials`
- `docker-registry-url`
- Database credentials
- Email credentials
- OAuth2 credentials

---

## 🛡️ Security Features

1. **Password Encryption:** BCrypt hashing
2. **JWT Tokens:** Secure token-based authentication
3. **OTP Verification:** 6-digit codes valid for 5 minutes
4. **OAuth2:** Industry-standard social login
5. **HTTPS Ready:** Nginx reverse proxy configuration included

---

## 📧 OTP Email Template

Users receive professional OTP emails:

```
Hello {username},

Your One-Time Password (OTP) for Mobile Plus is:

{OTP_CODE}

This OTP is valid for 5 minutes. Please do not share this code with anyone.

If you didn't request this code, please ignore this email.

Best regards,
Mobile Plus Team
```

---

## 🤖 Telegram Bot Commands

- `/start` - Welcome message and setup instructions
- `/getchatid` - Get your Telegram Chat ID for OTP delivery

---

## 📞 Support

For issues or questions:
- Create an issue on GitHub
- Contact: support@mobileplus.com

---

## 📄 License

This project is licensed under the MIT License.
