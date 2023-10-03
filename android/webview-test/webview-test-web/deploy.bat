chcp 65001
cd %~dp0.\

set TARGET_DIR=..\app\src\main\assets\dist

rmdir /s /q .\dist
rmdir /s /q %TARGET_DIR%
call npm run build
xcopy /y /e /i /q dist\* %TARGET_DIR%