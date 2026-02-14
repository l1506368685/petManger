@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo Running minimal test seed (clear + 2 members, 2 goods, 2 pets, 1 order)...
mysql -u root -proot --default-character-set=utf8mb4 pet_manager < clear_all_and_minimal_seed.sql
if errorlevel 1 ( echo Failed. & pause & exit /b 1 )
echo Done. Check member/goods in DB or frontend.
pause
