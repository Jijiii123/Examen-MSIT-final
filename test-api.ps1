# Quick Test Script for Media App gRPC
# Run this script in PowerShell after both servers are running

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Media App gRPC - Quick Test Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Test 1: Upload a video
Write-Host "[TEST 1] Uploading a video..." -ForegroundColor Yellow
try {
    $response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/videos/upload" -Method POST
    Write-Host "✅ SUCCESS - Video uploaded!" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor White
    $response1 | ConvertTo-Json -Depth 5
    Write-Host ""
} catch {
    Write-Host "❌ FAILED - Could not upload video" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Start-Sleep -Seconds 1

# Test 2: Get the video by ID
Write-Host "[TEST 2] Fetching video with ID 1..." -ForegroundColor Yellow
try {
    $response2 = Invoke-RestMethod -Uri "http://localhost:8080/api/videos/1" -Method GET
    Write-Host "✅ SUCCESS - Video retrieved!" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor White
    $response2 | ConvertTo-Json -Depth 5
    Write-Host ""
} catch {
    Write-Host "❌ FAILED - Could not retrieve video" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Start-Sleep -Seconds 1

# Test 3: Get creator information
Write-Host "[TEST 3] Fetching creator with ID 2..." -ForegroundColor Yellow
try {
    $response3 = Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2" -Method GET
    Write-Host "✅ SUCCESS - Creator retrieved!" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor White
    $response3 | ConvertTo-Json -Depth 5
    Write-Host ""
} catch {
    Write-Host "❌ FAILED - Could not retrieve creator" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Start-Sleep -Seconds 1

# Test 4: Get all videos by creator
Write-Host "[TEST 4] Fetching all videos by creator 2..." -ForegroundColor Yellow
try {
    $response4 = Invoke-RestMethod -Uri "http://localhost:8080/api/creators/2/videos" -Method GET
    Write-Host "✅ SUCCESS - Creator videos retrieved!" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor White
    $response4 | ConvertTo-Json -Depth 5
    Write-Host ""
} catch {
    Write-Host "❌ FAILED - Could not retrieve creator videos" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  All tests completed!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
