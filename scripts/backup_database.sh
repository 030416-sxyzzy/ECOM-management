#!/bin/bash

# 数据库备份脚本
# 使用方法: ./backup_database.sh [数据库名] [备份文件名]

# 数据库配置
DB_HOST="localhost"
DB_PORT="3306"
DB_USER="root"
DB_PASS="123456"
DB_NAME=${1:-"ecom_management"}

# 备份配置
BACKUP_DIR="/backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE=${2:-"${DB_NAME}_backup_${TIMESTAMP}.sql"}

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 执行备份
echo "开始备份数据库: $DB_NAME"
echo "备份文件: $BACKUP_DIR/$BACKUP_FILE"

mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" \
    --single-transaction \
    --routines \
    --triggers \
    --events \
    --add-drop-database \
    --add-drop-table \
    --add-locks \
    --disable-keys \
    --extended-insert \
    --quick \
    --lock-tables=false \
    "$DB_NAME" > "$BACKUP_DIR/$BACKUP_FILE"

# 检查备份是否成功
if [ $? -eq 0 ]; then
    echo "数据库备份成功!"
    echo "备份文件大小: $(du -h "$BACKUP_DIR/$BACKUP_FILE" | cut -f1)"
    echo "备份文件路径: $BACKUP_DIR/$BACKUP_FILE"
else
    echo "数据库备份失败!"
    exit 1
fi
