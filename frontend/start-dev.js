// 简化的开发服务器启动脚本
const { spawn } = require('child_process');
const path = require('path');

console.log('🚀 启动前端开发服务器...');

// 尝试使用 npx 启动 Vite
const viteProcess = spawn('npx', ['vite'], {
  cwd: __dirname,
  stdio: 'inherit',
  shell: true
});

viteProcess.on('error', (error) => {
  console.error('❌ 启动失败:', error.message);
  console.log('💡 请确保已安装 Node.js 和 npm');
  console.log('💡 或者手动运行: npm install && npm run dev');
});

viteProcess.on('close', (code) => {
  console.log(`📦 进程退出，代码: ${code}`);
});


