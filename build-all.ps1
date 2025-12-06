# Script PowerShell para compilar todos os microsserviços

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Compilando todos os microsserviços" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"
$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $baseDir

$services = @(
    @{Name="Usuários"; Path="./microservicos/usuarios"},
    @{Name="Pagamento"; Path="./microservicos/pagamento"},
    @{Name="Notificação"; Path="./microservicos/notificacao"},
    @{Name="Catálogo"; Path="./microservicos/livros"}
)

$success = $true
$count = 1

foreach ($service in $services) {
    Write-Host "`n[$count/4] Compilando Serviço de $($service.Name)..." -ForegroundColor Yellow
    
    Push-Location $service.Path
    
    try {
        $output = & .\mvnw.cmd clean package -DskipTests 2>&1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "ERRO ao compilar Serviço de $($service.Name)!" -ForegroundColor Red
            $success = $false
            Pop-Location
            break
        }
        Write-Host "✓ Compilado com sucesso!" -ForegroundColor Green
    }
    catch {
        Write-Host "ERRO: $_" -ForegroundColor Red
        $success = $false
        Pop-Location
        break
    }
    
    Pop-Location
    $count++
}

Write-Host "`n========================================" -ForegroundColor Cyan
if ($success) {
    Write-Host "Compilação concluída com SUCESSO!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "`nPara executar com Docker:" -ForegroundColor Yellow
    Write-Host "  docker-compose up --build" -ForegroundColor White
} else {
    Write-Host "Compilação FALHOU!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Cyan
}

Write-Host "`nPressione qualquer tecla para continuar..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
