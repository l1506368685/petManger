@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo [1/2] Clearing business tables...
mysql -u root -proot --default-character-set=utf8mb4 pet_manager < truncate_before_seed.sql
if errorlevel 1 ( echo Failed. & pause & exit /b 1 )
echo [2/2] Importing test data (UTF-8)...
mysql -u root -proot --default-character-set=utf8mb4 pet_manager < seed_test_data_one_month.sql
if errorlevel 1 ( echo Failed. & pause & exit /b 1 )
echo Done. Chinese text should display correctly.
pause
