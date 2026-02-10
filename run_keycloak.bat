@echo off
set "PATH=%PATH%;C:\Program Files\Docker\Docker\resources\bin"
echo Docker Path Added.
docker info > nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Docker daemon is not running. Please start Docker Desktop.
    exit /b 1
)
echo Starting Keycloak...
docker run -p 127.0.0.1:8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.5.2 start-dev
