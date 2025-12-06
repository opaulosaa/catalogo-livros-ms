@echo off
echo ========================================
echo Compilando todos os microsserviços
echo ========================================

cd /d "%~dp0"

echo.
echo [1/4] Compilando Serviço de Usuários...
cd "user_microsservice\usuarios"
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao compilar Serviço de Usuários!
    cd ..\..
    pause
    exit /b 1
)
cd ..\..

echo.
echo [2/4] Compilando Serviço de Pagamento...
cd "spring.boot.ms.pagamento"
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao compilar Serviço de Pagamento!
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo [3/4] Compilando Serviço de Notificação...
cd "AV3_catalogolivro_notification"
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao compilar Serviço de Notificação!
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo [4/4] Compilando Serviço de Catálogo...
cd "catalogo_livros"
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao compilar Serviço de Catálogo!
    cd ..
    pause
    exit /b 1
)
cd ..

echo.
echo ========================================
echo Compilação concluída com SUCESSO!
echo ========================================
echo.
echo Para executar com Docker:
echo   docker-compose up --build
echo.
pause
