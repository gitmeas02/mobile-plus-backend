# Mobile Plus Backend - Setup Guide

## 🎯 Quick Start Guide

This guide will help you set up the complete authentication system with OTP via Email/Telegram and Social Login.

---

## 📦 What's Included

✅ **Authentication Features:**
- Email/Password login with OTP verification
- Social login (Google, GitHub, Telegram)
- OTP delivery via Email or Telegram
- JWT token-based authentication
- User registration with validation

✅ **CI/CD:**
- GitHub Actions workflow
- Jenkins pipeline
- Docker containerization

---

## 🚀 Installation Steps

### Step 1: Clone the Repository
```bash
git clone https://github.com/gitmeas02/mobile-plus-backend.git
cd mobile-plus-backend
```

### Step 2: Configure Environment Variables

Copy the `.env` file and update with your credentials:

```env
# Database
MY_SQL_HOST=localhost
MY_SQL_PORT=3306
MY_SQL_USER=user_mobile_plus
MY_SQL_PASSWORD=YOUR_DB_PASSWORD
MY_SQL_DB=mobile_plus_db

# JWT
JWT_SECRET=YOUR_SUPER_SECRET_KEY_AT_LEAST_32_CHARACTERS
JWT_EXPIRATION_MS=86400000

# Email (Gmail)
MAIL_USERNAME=your.email@gmail.com
MAIL_PASSWORD=your_app_password

# Telegram Bot
TELEGRAM_BOT_TOKEN=123456789:ABCdefGHIjklMNOpqrsTUVwxyz
TELEGRAM_BOT_USERNAME=your_bot_name

# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-google-client-secret

# GitHub OAuth2
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

### Step 3: Setup Database

**Option A: Using MySQL directly**
```bash
mysql -u root -p

CREATE DATABASE mobile_plus_db;
CREATE USER 'user_mobile_plus'@'localhost' IDENTIFIED BY 'YOUR_PASSWORD';
GRANT ALL PRIVILEGES ON mobile_plus_db.* TO 'user_mobile_plus'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

**Option B: Using Docker Compose**
The database will be automatically created when you run:
```bash
docker-compose up -d mysql
```

### Step 4: Build the Application

```bash
# Make gradlew executable (Linux/Mac)
chmod +x gradlew

# Build the project
./gradlew clean build
```

### Step 5: Run the Application

**Option A: Using Gradle**
```bash
./gradlew bootRun
```

**Option B: Using Docker Compose**
```bash
docker-compose up -d
```

**Option C: Using JAR**
```bash
java -jar build/libs/mobile-0.0.1-SNAPSHOT.jar
```

Application will be available at: `http://localhost:8080`

---

## 🔧 Configuration Guides

### 1. Gmail App Password Setup

1. **Enable 2-Step Verification:**
   - Go to https://myaccount.google.com/security
   - Click "2-Step Verification"
   - Follow the setup process

2. **Generate App Password:**
   - Go to https://myaccount.google.com/apppasswords
   - Select "Mail" and your device
   - Click "Generate"
   - Copy the 16-character password (remove spaces)
   - Use this as `MAIL_PASSWORD` in `.env`

### 2. Telegram Bot Setup

1. **Create Bot:**
   - Open Telegram and search for `@BotFather`
   - Send `/newbot` command
   - Choose a name for your bot (e.g., "Mobile Plus OTP Bot")
   - Choose a username (must end with 'bot', e.g., "mobile_plus_otp_bot")
   - BotFather will give you a token - save this as `TELEGRAM_BOT_TOKEN`

2. **Get Chat ID:**
   - Users need to:
     - Start your bot in Telegram
     - Send `/getchatid` command
     - Copy the Chat ID shown
     - Use this when registering with `telegramChatId` field

### 3. Google OAuth2 Setup

1. **Create Google Cloud Project:**
   - Go to https://console.cloud.google.com/
   - Create a new project or select existing one
   - Enable "Google+ API"

2. **Create OAuth Credentials:**
   - Go to "APIs & Services" > "Credentials"
   - Click "Create Credentials" > "OAuth 2.0 Client ID"
   - Application type: "Web application"
   - Add authorized redirect URIs:
     ```
     http://localhost:8080/login/oauth2/code/google
     http://localhost:8080/oauth2/callback/google
     ```
   - Click "Create"
   - Copy `Client ID` and `Client Secret`

### 4. GitHub OAuth2 Setup

1. **Register OAuth App:**
   - Go to https://github.com/settings/developers
   - Click "New OAuth App"
   - Fill in:
     - Application name: "Mobile Plus Backend"
     - Homepage URL: `http://localhost:8080`
     - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
   - Click "Register application"
   - Copy `Client ID`
   - Click "Generate a new client secret" and copy it

---

## 🧪 Testing the API

### 1. Health Check
```bash
curl http://localhost:8080/auth/health
```

### 2. Register User (with Email OTP)
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "username": "johndoe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "otpDeliveryMethod": "email"
  }'
```

**Expected Response:**
```json
{
  "message": "Registration successful! Please verify your OTP sent to your email",
  "requiresOtp": true,
  "email": "john@example.com"
}
```

**Check your email** for the OTP code!

### 3. Register User (with Telegram OTP)
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "username": "janesmith",
    "email": "jane@example.com",
    "password": "SecurePass456",
    "telegramChatId": "YOUR_TELEGRAM_CHAT_ID",
    "otpDeliveryMethod": "telegram"
  }'
```

**Check your Telegram** for the OTP code!

### 4. Verify OTP
```bash
curl -X POST http://localhost:8080/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "otpCode": "123456"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "uuid-here",
  "username": "johndoe",
  "email": "john@example.com",
  "message": "Login successful!"
}
```

### 5. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

You'll receive another OTP. Verify it using the verify-otp endpoint.

### 6. Social Login

**Google Login:**
Open in browser: `http://localhost:8080/oauth2/authorization/google`

**GitHub Login:**
Open in browser: `http://localhost:8080/oauth2/authorization/github`

After successful OAuth, you'll be redirected to:
```
http://localhost:3000/auth/callback?token={JWT_TOKEN}&user={USERNAME}&email={EMAIL}
```

---

## 🐳 Docker Commands

**Start all services:**
```bash
docker-compose up -d
```

**View logs:**
```bash
docker-compose logs -f backend
```

**Stop all services:**
```bash
docker-compose down
```

**Rebuild and restart:**
```bash
docker-compose up -d --build
```

---

## 🔍 Troubleshooting

### Issue: Email not sending
**Solution:**
1. Verify Gmail App Password is correct
2. Check if 2-Step Verification is enabled
3. Ensure "Less secure app access" is OFF (use App Passwords instead)
4. Check application logs for detailed error

### Issue: Telegram OTP not received
**Solution:**
1. Verify bot token is correct
2. Ensure user has started the bot (`/start`)
3. Verify Chat ID is correct (use `/getchatid`)
4. Check if bot is running

### Issue: OAuth2 not working
**Solution:**
1. Verify redirect URIs match exactly in provider console
2. Check Client ID and Secret are correct
3. Ensure OAuth2 dependencies are loaded (restart app)
4. Check browser console for errors

### Issue: Database connection failed
**Solution:**
1. Verify MySQL is running: `mysql -u root -p`
2. Check credentials in `.env` file
3. Ensure database exists
4. Check if port 3306 is not blocked

---

## 📊 CI/CD Setup

### GitHub Actions

1. **Add Repository Secrets:**
   - Go to repository Settings → Secrets and variables → Actions
   - Add the following secrets:
     - `DOCKER_USERNAME` - Your Docker Hub username
     - `DOCKER_PASSWORD` - Your Docker Hub password/token
     - `DEPLOY_HOST` - Server IP address
     - `DEPLOY_USER` - SSH username
     - `DEPLOY_SSH_KEY` - Private SSH key

2. **Workflow triggers automatically** on push to `main` or `develop` branches

### Jenkins

1. **Install Required Plugins:**
   - Docker Pipeline
   - Email Extension
   - JDK 21

2. **Configure Tools:**
   - Manage Jenkins → Tools
   - Add JDK 21 installation
   - Add Docker installation

3. **Add Credentials:**
   - Add Docker Hub credentials (ID: `docker-hub-credentials`)
   - Add all environment variables from `.env`

4. **Create Pipeline:**
   - New Item → Pipeline
   - Point to your `Jenkinsfile`
   - Save and build

---

## 📈 Monitoring

**Application Logs:**
```bash
# Docker
docker-compose logs -f backend

# Local
tail -f logs/spring.log
```

**Database:**
```bash
docker-compose exec mysql mysql -u user_mobile_plus -p mobile_plus_db
```

---

## 🎓 Next Steps

1. ✅ Test all authentication endpoints
2. ✅ Configure production environment variables
3. ✅ Set up SSL certificates for production
4. ✅ Configure email templates (optional)
5. ✅ Set up monitoring and logging
6. ✅ Deploy to production server

---

## 📞 Support

Need help? Check:
- [API Documentation](./API_DOCUMENTATION.md)
- [GitHub Issues](https://github.com/gitmeas02/mobile-plus-backend/issues)
- Email: support@mobileplus.com

---

## ✅ Verification Checklist

- [ ] Database is running and accessible
- [ ] All environment variables are configured
- [ ] Application starts without errors
- [ ] Email OTP is received successfully
- [ ] Telegram OTP is received successfully
- [ ] Google OAuth login works
- [ ] GitHub OAuth login works
- [ ] JWT tokens are generated correctly
- [ ] Docker Compose works
- [ ] CI/CD pipeline is configured

**Congratulations! Your Mobile Plus Backend is ready! 🎉**
