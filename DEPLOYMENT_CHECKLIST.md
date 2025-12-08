# ✅ Mobile Plus Backend - Setup Checklist

Use this checklist to ensure everything is configured correctly before running the application.

---

## 📋 Pre-Deployment Checklist

### Database Setup
- [ ] MySQL 8.0 or higher installed
- [ ] Database `mobile_plus_db` created
- [ ] Database user `user_mobile_plus` created with proper permissions
- [ ] Database credentials added to `.env` file
- [ ] Connection tested successfully

### Environment Variables
- [ ] `.env` file created from `.env.example`
- [ ] `MY_SQL_HOST` configured
- [ ] `MY_SQL_PORT` configured (default: 3306)
- [ ] `MY_SQL_USER` configured
- [ ] `MY_SQL_PASSWORD` configured
- [ ] `MY_SQL_DB` configured
- [ ] `JWT_SECRET` set (minimum 32 characters)
- [ ] `JWT_EXPIRATION_MS` set (86400000 = 24 hours)

### Email Configuration (Gmail)
- [ ] Gmail account available
- [ ] 2-Factor Authentication enabled on Gmail
- [ ] App Password generated (16 characters)
- [ ] `MAIL_USERNAME` set in `.env`
- [ ] `MAIL_PASSWORD` set in `.env` (App Password)
- [ ] Test email sent successfully

### Telegram Bot Setup
- [ ] Telegram bot created via @BotFather
- [ ] Bot token obtained
- [ ] Bot username noted
- [ ] `TELEGRAM_BOT_TOKEN` set in `.env`
- [ ] `TELEGRAM_BOT_USERNAME` set in `.env`
- [ ] Bot tested with `/start` command
- [ ] Chat ID obtained using `/getchatid`

### Google OAuth2 Setup
- [ ] Google Cloud Project created
- [ ] Google+ API enabled
- [ ] OAuth 2.0 credentials created
- [ ] Redirect URI added: `http://localhost:8080/login/oauth2/code/google`
- [ ] Production redirect URI added (if deploying)
- [ ] `GOOGLE_CLIENT_ID` set in `.env`
- [ ] `GOOGLE_CLIENT_SECRET` set in `.env`

### GitHub OAuth2 Setup
- [ ] GitHub OAuth App created
- [ ] Authorization callback URL set: `http://localhost:8080/login/oauth2/code/github`
- [ ] Production callback URL added (if deploying)
- [ ] `GITHUB_CLIENT_ID` set in `.env`
- [ ] `GITHUB_CLIENT_SECRET` set in `.env`

### Build Environment
- [ ] Java 21 installed and configured
- [ ] JAVA_HOME environment variable set
- [ ] Gradle 8.5 or higher available
- [ ] `./gradlew --version` runs successfully
- [ ] Project builds without errors: `./gradlew clean build`

### Docker Setup (if using Docker)
- [ ] Docker installed
- [ ] Docker Compose installed
- [ ] Docker daemon running
- [ ] `docker --version` works
- [ ] `docker-compose --version` works
- [ ] User has permissions to run Docker commands

---

## 🧪 Testing Checklist

### Unit Tests
- [ ] All tests pass: `./gradlew test`
- [ ] Test coverage is acceptable
- [ ] No failing tests

### Health Check
- [ ] Application starts successfully
- [ ] Health endpoint responds: `curl http://localhost:8080/auth/health`
- [ ] No errors in startup logs

### Authentication Flow Tests

#### Registration with Email OTP
- [ ] User registration endpoint works
- [ ] OTP email is received
- [ ] Email template looks professional
- [ ] OTP code is 6 digits
- [ ] OTP verification works
- [ ] JWT token is generated
- [ ] Invalid OTP is rejected
- [ ] Expired OTP is rejected

#### Registration with Telegram OTP
- [ ] User registration with Telegram works
- [ ] OTP message received on Telegram
- [ ] Message formatting is correct
- [ ] Telegram OTP verification works

#### Login Flow
- [ ] Login with email works
- [ ] Login with username works
- [ ] OTP is sent after login
- [ ] OTP verification completes login
- [ ] JWT token is returned
- [ ] Wrong password is rejected

#### OTP Management
- [ ] Resend OTP works
- [ ] Can switch between email/Telegram delivery
- [ ] OTP expires after 5 minutes
- [ ] Only one valid OTP per user at a time

#### Social Login
- [ ] Google OAuth flow works
- [ ] GitHub OAuth flow works
- [ ] New user created on first OAuth login
- [ ] Existing user linked on OAuth login
- [ ] JWT token generated after OAuth
- [ ] User redirected correctly with token

---

## 🐳 Docker Checklist

### Docker Build
- [ ] Dockerfile builds successfully
- [ ] Docker image size is reasonable
- [ ] Multi-stage build optimized
- [ ] Health check configured in Dockerfile

### Docker Compose
- [ ] `docker-compose.yml` configured
- [ ] MySQL service starts
- [ ] Backend service starts
- [ ] Services can communicate
- [ ] Environment variables passed correctly
- [ ] Volumes configured for persistence
- [ ] Networks configured properly
- [ ] Health checks working

### Docker Testing
- [ ] `docker-compose up -d` works
- [ ] All containers are running: `docker-compose ps`
- [ ] Logs are accessible: `docker-compose logs`
- [ ] Can connect to MySQL container
- [ ] Backend responds to requests
- [ ] Graceful shutdown works: `docker-compose down`

---

## 🔄 CI/CD Checklist

### GitHub Actions
- [ ] Workflow file exists: `.github/workflows/ci-cd.yml`
- [ ] GitHub Actions enabled for repository
- [ ] Required secrets added to GitHub:
  - [ ] `DOCKER_USERNAME`
  - [ ] `DOCKER_PASSWORD`
  - [ ] `DEPLOY_HOST`
  - [ ] `DEPLOY_USER`
  - [ ] `DEPLOY_SSH_KEY`
- [ ] Workflow runs on push to main/develop
- [ ] Build stage completes successfully
- [ ] Test stage passes
- [ ] Docker image is built and pushed
- [ ] Deployment stage works (if configured)

### Jenkins Setup
- [ ] Jenkins installed and running
- [ ] JDK 21 configured in Jenkins
- [ ] Docker plugin installed
- [ ] Required credentials added:
  - [ ] `docker-hub-credentials`
  - [ ] `docker-registry-url`
  - [ ] Database credentials
  - [ ] Email credentials
  - [ ] OAuth2 credentials
- [ ] Pipeline job created
- [ ] Jenkinsfile detected
- [ ] Pipeline runs successfully
- [ ] Email notifications configured

---

## 🚀 Deployment Checklist

### Pre-Deployment
- [ ] All tests passing
- [ ] Environment variables for production configured
- [ ] Database backup taken
- [ ] SSL/TLS certificates obtained
- [ ] Domain name configured
- [ ] Firewall rules configured
- [ ] Monitoring tools set up

### Production Environment
- [ ] Production `.env` file configured
- [ ] Production database created
- [ ] Production database user created
- [ ] HTTPS redirect configured
- [ ] Security headers configured
- [ ] Rate limiting enabled
- [ ] CORS configured for production domains
- [ ] Logging configured
- [ ] Error tracking set up

### Post-Deployment
- [ ] Application accessible via domain
- [ ] HTTPS working
- [ ] Health check endpoint responds
- [ ] Can register new user
- [ ] Email OTP received in production
- [ ] Telegram OTP works in production
- [ ] Google OAuth works with production URLs
- [ ] GitHub OAuth works with production URLs
- [ ] Database queries working
- [ ] Logs being generated
- [ ] Monitoring showing metrics
- [ ] Backup schedule configured

---

## 📊 Performance Checklist

### Optimization
- [ ] Database indexes created
- [ ] Connection pooling configured
- [ ] Cache headers configured
- [ ] Gzip compression enabled
- [ ] Static assets optimized
- [ ] Query performance acceptable
- [ ] API response times < 200ms
- [ ] Memory usage acceptable
- [ ] CPU usage acceptable

---

## 🔒 Security Checklist

### Application Security
- [ ] Passwords hashed with BCrypt
- [ ] JWT secret is strong and unique
- [ ] JWT tokens expire appropriately
- [ ] SQL injection protection enabled
- [ ] XSS protection enabled
- [ ] CSRF protection configured
- [ ] Security headers configured
- [ ] Input validation in place
- [ ] Error messages don't leak information

### Infrastructure Security
- [ ] Database not publicly accessible
- [ ] Firewall configured
- [ ] SSH keys used for deployment
- [ ] Secrets not in version control
- [ ] Environment variables properly secured
- [ ] Docker containers run as non-root
- [ ] Dependencies up to date
- [ ] Security patches applied

---

## 📝 Documentation Checklist

### Code Documentation
- [ ] README.md complete
- [ ] API_DOCUMENTATION.md complete
- [ ] SETUP_GUIDE.md complete
- [ ] IMPLEMENTATION_SUMMARY.md complete
- [ ] Code comments adequate
- [ ] Complex logic explained

### Operational Documentation
- [ ] Deployment runbook created
- [ ] Troubleshooting guide available
- [ ] Backup/restore procedure documented
- [ ] Disaster recovery plan in place
- [ ] On-call procedures documented

---

## ✅ Final Verification

Before marking complete:
- [ ] All sections above are checked
- [ ] Application runs locally
- [ ] All endpoints tested
- [ ] Docker containers work
- [ ] CI/CD pipeline runs
- [ ] Documentation reviewed
- [ ] Team trained on deployment
- [ ] Monitoring alerts configured
- [ ] Backup tested
- [ ] Rollback plan in place

---

## 🎉 Ready for Production!

Once all items are checked, your Mobile Plus Backend is ready for production deployment!

**Remember to:**
- Monitor logs after deployment
- Test all features in production
- Have rollback plan ready
- Keep team informed of deployment
- Update documentation as needed

---

**Date Completed:** _________________

**Deployed By:** _________________

**Production URL:** _________________

**Notes:** 
```
_________________________________________
_________________________________________
_________________________________________
```
