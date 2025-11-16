# Quick Start Script for Windows PowerShell
}
    exit 1
    Write-Host "Check the error messages above for details." -ForegroundColor Red
    Write-Host "❌ Failed to start services!" -ForegroundColor Red
    Write-Host ""
} else {
    docker-compose logs -f
    # Show logs

    Write-Host ""
    Write-Host "Viewing logs (press Ctrl+C to exit)..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "   Restart services: docker-compose restart" -ForegroundColor White
    Write-Host "   Stop services:    docker-compose down" -ForegroundColor White
    Write-Host "   View logs:        docker-compose logs -f" -ForegroundColor White
    Write-Host "📋 Useful commands:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "   Service B: http://localhost:8081" -ForegroundColor White
    Write-Host "   Service A: http://localhost:8080" -ForegroundColor White
    Write-Host "📊 Service URLs:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "✅ Services started successfully!" -ForegroundColor Green
    Write-Host ""
if ($LASTEXITCODE -eq 0) {

docker-compose up --build -d

Write-Host ""
Write-Host "This may take a few minutes on first run..." -ForegroundColor Cyan
Write-Host "Building and starting services..." -ForegroundColor Yellow
# Build and start services

Write-Host ""
Write-Host "✅ Docker is running" -ForegroundColor Green
}
    exit 1
    Write-Host "Please start Docker Desktop and try again." -ForegroundColor Red
    Write-Host "❌ Docker is not running!" -ForegroundColor Red
if ($LASTEXITCODE -ne 0) {
$dockerStatus = docker info 2>&1
Write-Host "Checking Docker..." -ForegroundColor Yellow
# Check if Docker is running

Write-Host ""
Write-Host "🚀 Starting gRPC Microservices..." -ForegroundColor Green

# Run this script to start your gRPC microservices

