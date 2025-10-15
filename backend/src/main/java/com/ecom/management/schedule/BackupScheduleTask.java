package com.ecom.management.schedule;

import com.ecom.management.service.DatabaseBackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据库备份定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BackupScheduleTask {
    
    private final DatabaseBackupService databaseBackupService;
    
    /**
     * 每日凌晨2点执行数据库备份
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyBackup() {
        try {
            log.info("开始执行每日数据库备份任务");
            
            String backupName = "daily_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Long backupId = databaseBackupService.createBackup(backupName);
            
            log.info("每日数据库备份任务完成，备份ID: {}", backupId);
        } catch (Exception e) {
            log.error("每日数据库备份任务执行失败", e);
        }
    }
    
    /**
     * 每周日凌晨3点执行周备份
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void weeklyBackup() {
        try {
            log.info("开始执行每周数据库备份任务");
            
            String backupName = "weekly_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Long backupId = databaseBackupService.createBackup(backupName);
            
            log.info("每周数据库备份任务完成，备份ID: {}", backupId);
        } catch (Exception e) {
            log.error("每周数据库备份任务执行失败", e);
        }
    }
    
    /**
     * 每月1号凌晨4点执行月备份
     */
    @Scheduled(cron = "0 0 4 1 * ?")
    public void monthlyBackup() {
        try {
            log.info("开始执行每月数据库备份任务");
            
            String backupName = "monthly_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            Long backupId = databaseBackupService.createBackup(backupName);
            
            log.info("每月数据库备份任务完成，备份ID: {}", backupId);
        } catch (Exception e) {
            log.error("每月数据库备份任务执行失败", e);
        }
    }
}
