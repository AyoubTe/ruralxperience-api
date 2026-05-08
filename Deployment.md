# 🌿 RuralXperience — EC2 Deployment Guide

Complete guide to deploying the Spring Boot API on Ubuntu EC2 with Docker, systemd, and Nginx reverse proxy.

---

## 📋 Prerequisites

- Ubuntu 22.04+ EC2 instance (t2.micro or higher)
- SSH access with your `.pem` key
- AWS Security Group with ports **22**, **80**, **443** open
- Your Spring Boot project pushed to the server

---

## 1. Connect to EC2

```bash
ssh -i your-key.pem ubuntu@YOUR_EC2_PUBLIC_IP
```

---

## 2. Update the System

```bash
sudo apt update && sudo apt upgrade -y
```

---

## 3. Install Java 21

```bash
sudo apt install -y openjdk-21-jdk
java -version
```

Expected output:
```
openjdk version "21.x.x"
```

---

## 4. Install Maven

```bash
sudo apt install -y maven
mvn -version
```

---

## 5. Install Docker & Docker Compose

```bash
# Add Docker's GPG key
sudo apt install -y ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Add Docker repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Remove conflicting packages and install Docker
sudo apt remove -y docker-compose-v2
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Allow ubuntu user to run Docker without sudo
sudo usermod -aG docker ubuntu
newgrp docker

# Verify
docker --version
docker compose version
```

---

## 6. Clone or Upload Your Project

```bash
# Option A — Clone from GitHub
git clone https://github.com/AyoubTe/ruralxperience-api.git
cd ruralxperience-api

# Option B — Upload via SCP (from your local machine)
scp -i your-key.pem -r ./ruralxperience-api ubuntu@YOUR_EC2_IP:/home/ubuntu/
```

---

## 7. Start Docker Services (PostgreSQL, Redis, Mailhog)

Your `docker-compose.yml` contains PostgreSQL, Redis, and Mailhog. Start them:

```bash
cd ~/ruralxperience-api
docker compose up -d
```

Verify all containers are running:
```bash
docker compose ps
```

Expected output:
```
NAME           IMAGE                  STATUS
rxp-postgres   postgres:16-alpine     Up (healthy)
rxp-redis      redis:7-alpine         Up (healthy)
rxp-mailhog    mailhog/mailhog        Up
```

---

## 8. Test the Application Manually

Before creating a service, verify the app starts correctly:

```bash
cd ~/ruralxperience-api
chmod +x mvnw

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ruralxperience \
POSTGRES_USER=rxp_user \
POSTGRES_PASSWORD=rxp_secret \
mvn spring-boot:run
```

You should see:
```
Tomcat started on port 8080
Started RuralXperienceApplication in X seconds
```

Test locally:
```bash
curl http://localhost:8080/actuator/health
# Expected: {"status":"UP"}
```

Press `Ctrl+C` to stop once confirmed.

---

## 9. Deploy as a systemd Service

Create the service file:

```bash
sudo nano /etc/systemd/system/ruralxperience.service
```

Paste the following (replace values as needed):

```ini
[Unit]
Description=RuralXperience Spring Boot API
After=network.target docker.service

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu/ruralxperience-api
ExecStart=/usr/bin/mvn spring-boot:run
Restart=always
RestartSec=10
Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ruralxperience"
Environment="POSTGRES_USER=rxp_user"
Environment="POSTGRES_PASSWORD=rxp_secret"
Environment="JWT_SECRET=your-strong-secret-key-minimum-32-chars-change-this"
Environment="CORS_ORIGINS=*"
Environment="EMAIL_ENABLED=false"
Environment="STORAGE_TYPE=local"

[Install]
WantedBy=multi-user.target
```

Save with `Ctrl+X → Y → Enter`.

Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable ruralxperience
sudo systemctl start ruralxperience
sudo systemctl status ruralxperience
```

Expected output:
```
● ruralxperience.service - RuralXperience Spring Boot API
     Active: active (running)
```

### Useful Service Commands

```bash
# View live logs
sudo journalctl -u ruralxperience -f

# Restart the service
sudo systemctl restart ruralxperience

# Stop the service
sudo systemctl stop ruralxperience

# Check status
sudo systemctl status ruralxperience
```

---

## 10. Install and Configure Nginx Reverse Proxy

### Install Nginx

```bash
sudo apt install -y nginx
sudo systemctl enable nginx
sudo systemctl start nginx
```

### Remove the Default Site

```bash
sudo rm /etc/nginx/sites-enabled/default
```

### Create the Proxy Configuration

```bash
sudo nano /etc/nginx/sites-available/ruralxperience
```

Paste the following (replace `YOUR_EC2_PUBLIC_IP` with your actual IP or domain):

```nginx
server {
    listen 80;
    server_name YOUR_EC2_PUBLIC_IP;

    # Spring Boot API
    location /api/v1/ {
        proxy_pass http://localhost:8080/api/v1/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Actuator health check
    location /actuator/ {
        proxy_pass http://localhost:8080/actuator/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # File uploads
    location /files/ {
        proxy_pass http://localhost:8080/files/;
        proxy_set_header Host $host;
    }
}
```

### Enable the Site

```bash
sudo ln -s /etc/nginx/sites-available/ruralxperience /etc/nginx/sites-enabled/

# Test configuration
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx
```

Expected output from `nginx -t`:
```
nginx: the configuration file /etc/nginx/nginx.conf syntax is ok
nginx: configuration file /etc/nginx/nginx.conf test is successful
```

---

## 11. Verify Everything Works

```bash
# Health check through Nginx
curl http://YOUR_EC2_PUBLIC_IP/actuator/health
# Expected: {"status":"UP"}

# API through Nginx
curl http://YOUR_EC2_PUBLIC_IP/api/v1/experiences
# Expected: {"content":[...], "totalElements": X}
```

---

## 12. AWS Security Group Rules

Make sure your EC2 Security Group has these inbound rules:

| Type       | Protocol | Port | Source    |
|------------|----------|------|-----------|
| SSH        | TCP      | 22   | Your IP   |
| HTTP       | TCP      | 80   | 0.0.0.0/0 |
| HTTPS      | TCP      | 443  | 0.0.0.0/0 |

> ⚠️ Port 8080 does **not** need to be open publicly — all traffic goes through Nginx on port 80.

---

## 13. Update Frontend Environment

On your local machine, update `.env`:

```env
VITE_API_BASE_URL=http://YOUR_EC2_PUBLIC_IP/api/v1
```

Rebuild and sync for mobile:

```bash
npm run build
npx cap sync
```

---

## 14. (Optional) Set Up HTTPS with Let's Encrypt

If you have a domain name pointed to your EC2 IP:

```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d yourdomain.com -d www.yourdomain.com
```

Certbot will automatically update your Nginx config to use HTTPS. Then update your frontend:

```env
VITE_API_BASE_URL=https://yourdomain.com/api/v1
```

Certificates auto-renew. Test renewal with:
```bash
sudo certbot renew --dry-run
```

---

## 🏗️ Architecture Overview

```
Internet
    │
    ▼
[AWS Security Group]
    │  Port 80/443
    ▼
[Nginx Reverse Proxy]
    │  localhost:8080
    ▼
[Spring Boot API]  ──────►  [PostgreSQL :5432]  (Docker)
                   ──────►  [Redis :6379]        (Docker)
                   ──────►  [Mailhog :1025]      (Docker)
```

---

## 🔁 Full Restart Procedure

If you ever need to restart everything from scratch:

```bash
# 1. Start Docker services
cd ~/ruralxperience-api
docker compose up -d

# 2. Restart Spring Boot
sudo systemctl restart ruralxperience

# 3. Restart Nginx
sudo systemctl restart nginx

# 4. Verify
curl http://localhost:8080/actuator/health
curl http://YOUR_EC2_PUBLIC_IP/api/v1/experiences
```

---

## 📁 File Locations Reference

| File | Path |
|------|------|
| Service file | `/etc/systemd/system/ruralxperience.service` |
| Nginx config | `/etc/nginx/sites-available/ruralxperience` |
| Nginx enabled | `/etc/nginx/sites-enabled/ruralxperience` |
| App directory | `/home/ubuntu/ruralxperience-api` |
| Uploaded files | `/home/ubuntu/ruralxperience-api/uploads` |
| App logs | `sudo journalctl -u ruralxperience -f` |
| Nginx logs | `/var/log/nginx/access.log` & `error.log` |