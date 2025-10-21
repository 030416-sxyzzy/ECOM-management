@echo off
chcp 65001 >nul
echo ======================================
echo    启动电商管理系统前端服务器
echo ======================================
echo.

cd /d "%~dp0"

echo [1/3] 检查 Node.js 环境...
where node >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 未找到 Node.js，请先安装 Node.js
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

node -v
npm -v
echo.

echo [2/3] 检查依赖包...
if not exist "node_modules\" (
    echo 依赖包不存在，正在安装...
    npm install
    if errorlevel 1 (
        echo [ERROR] 依赖安装失败
        pause
        exit /b 1
    )
) else (
    echo 依赖包已存在
)
echo.

echo [3/3] 启动开发服务器...
echo 前端地址: http://localhost:3000
echo 后端代理: http://localhost:8080
echo.
echo 按 Ctrl+C 可以停止服务器
echo ======================================
echo.

npm run dev

pause

