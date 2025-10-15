#!/bin/bash

# 数据库恢复脚本
# 使用方法: ./restore_database.sh [数据库名] [备份文件路径]

# 数据库配置
DB_HOST="localhost"
DB_PORT="3306"
DB_USER="root"
DB_PASS="123456"
DB_NAME=${1:-"ecom_management"}
BACKUP_FILE=${2}

# 检查参数
if [ -z "$BACKUP_FILE" ]; then
    echo "错误: 请指定备份文件路径"
    echo "使用方法: $0 [数据库名] [备份文件路径]"
    exit 1
fi

# 检查备份文件是否存在
if [ ! -f "$BACKUP_FILE" ]; then
    echo "错误: 备份文件不存在: $BACKUP_FILE"
    exit 1
fi

# 确认操作
echo "警告: 此操作将覆盖数据库 $DB_NAME 的所有数据!"
echo "备份文件: $BACKUP_FILE"
read -p "确定要继续吗? (y/N): " confirm

if [[ $confirm != [yY] ]]; then
    echo "操作已取消"
    exit 0
fi

# 执行恢复
echo "开始恢复数据库: $DB_NAME"
echo "从备份文件: $BACKUP_FILE"

mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$BACKUP_FILE"

# 检查恢复是否成功
if [ $? -eq 0 ]; then
    echo "数据库恢复成功!"
else
    echo "数据库恢复失败!"
    exit 1
fi
