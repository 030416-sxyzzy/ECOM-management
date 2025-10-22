-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ecom_management
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `ecom_management`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ecom_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ecom_management`;

--
-- Table structure for table `backup_records`
--

DROP TABLE IF EXISTS `backup_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backup_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `backup_file_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `backup_file_path` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `backup_file_size` bigint DEFAULT '0',
  `backup_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'manual',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'success',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backup_records`
--

LOCK TABLES `backup_records` WRITE;
/*!40000 ALTER TABLE `backup_records` DISABLE KEYS */;
INSERT INTO `backup_records` VALUES (5,'backup_20251022_125205.sql','database/backups/backup_20251022_125205.sql',25305,'manual','success','1','2025-10-22 04:52:06'),(6,'backup_20251022_220112.sql','database/backups/backup_20251022_220112.sql',26479,'manual','success','2','2025-10-22 14:01:12'),(7,'backup_20251022_220131.sql','database/backups/backup_20251022_220131.sql',26479,'manual','success','2','2025-10-22 14:01:31'),(8,'backup_20251022_220204.sql','database/backups/backup_20251022_220204.sql',26620,'manual','success','手动备份','2025-10-22 14:02:04'),(9,'backup_20251022_220227.sql','database/backups/backup_20251022_220227.sql',0,'manual','in_progress','重新备份','2025-10-22 14:02:27');
/*!40000 ALTER TABLE `backup_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '数量',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '分类描述',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'电子产品','各类电子设备及配件','2025-10-15 12:07:25','2025-10-15 12:07:25'),(2,'服装鞋帽','时尚服装和鞋类产品','2025-10-15 12:07:25','2025-10-15 12:07:25'),(3,'家居生活','家居用品和生活用品','2025-10-15 12:07:25','2025-10-15 12:07:25'),(4,'图书文具','图书、文具和办公用品','2025-10-15 12:07:25','2025-10-15 12:07:25'),(5,'运动户外','运动装备和户外用品','2025-10-15 12:07:25','2025-10-15 12:07:25');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_logs`
--

DROP TABLE IF EXISTS `inventory_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `change_type` enum('in','out') NOT NULL,
  `quantity` int NOT NULL,
  `reason` varchar(500) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `inventory_logs_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_logs`
--

LOCK TABLES `inventory_logs` WRITE;
/*!40000 ALTER TABLE `inventory_logs` DISABLE KEYS */;
INSERT INTO `inventory_logs` VALUES (1,5,'out',1,'Order ID: 5','2025-10-21 15:10:27'),(2,5,'out',1,'Order ID: 6','2025-10-21 16:04:50'),(3,1,'out',1,'Order ID: 6','2025-10-21 16:04:50'),(4,2,'out',1,'Order ID: 6','2025-10-21 16:04:50'),(5,6,'out',1,'Order ID: 7','2025-10-22 13:56:53'),(6,7,'out',1,'Order ID: 7','2025-10-22 13:56:53');
/*!40000 ALTER TABLE `inventory_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '购买数量',
  `price` decimal(10,2) NOT NULL COMMENT '购买时价格',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,3,1,899.00,'2025-10-21 14:29:03'),(2,1,4,1,99.00,'2025-10-21 14:29:03'),(3,1,7,1,199.00,'2025-10-21 14:29:03'),(4,1,5,1,1299.00,'2025-10-21 14:29:03'),(5,1,6,1,89.00,'2025-10-21 14:29:03'),(6,2,1,1,7999.00,'2025-10-21 14:34:13'),(7,3,6,1,89.00,'2025-10-21 14:42:41'),(8,4,7,1,199.00,'2025-10-21 14:47:02'),(9,5,5,1,1299.00,'2025-10-21 15:10:27'),(10,6,5,1,1299.00,'2025-10-21 16:04:50'),(11,6,1,1,7999.00,'2025-10-21 16:04:50'),(12,6,2,1,8999.00,'2025-10-21 16:04:50'),(13,7,6,1,89.00,'2025-10-22 13:56:53'),(14,7,7,1,199.00,'2025-10-22 13:56:53');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = gbk */ ;
/*!50003 SET character_set_results = gbk */ ;
/*!50003 SET collation_connection  = gbk_chinese_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_order_items_after_insert` AFTER INSERT ON `order_items` FOR EACH ROW BEGIN
    UPDATE products 
    SET stock = stock - NEW.quantity,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.product_id;
    
    INSERT INTO inventory_logs (product_id, change_type, quantity, reason)
    VALUES (NEW.product_id, 'out', NEW.quantity, CONCAT('Order ID: ', NEW.order_id));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` enum('pending','paid','shipped','completed','cancelled') COLLATE utf8mb4_unicode_ci DEFAULT 'pending' COMMENT '订单状态',
  `shipping_address` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货地址',
  `receiver_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `receiver_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `receiver_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_number` (`order_number`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,'ORD20251021222903EB7B7C',2585.00,'paid','默认收货人, 13800138000, 默认收货地址',NULL,NULL,NULL,'2025-10-21 14:29:03','2025-10-21 14:29:41'),(2,1,'ORD2025102122341368966A',7999.00,'shipped','默认收货人, 13800138000, 默认收货地址',NULL,NULL,NULL,'2025-10-21 14:34:13','2025-10-22 04:09:02'),(3,1,'ORD202510212242414756A7',89.00,'pending','默认收货人, 13800138000, 北京市朝阳区xx街道xx号','默认收货人','13800138000','北京市朝阳区xx街道xx号','2025-10-21 14:42:41','2025-10-21 14:42:41'),(4,1,'ORD20251021224702AB60D3',199.00,'pending','zzy, 13635990528, xxxxxxx','zzy','13635990528','xxxxxxx','2025-10-21 14:47:02','2025-10-21 14:47:02'),(5,1,'ORD20251021231027C24718',1299.00,'pending','yyy, 13635990528, yyyyyyyy','yyy','13635990528','yyyyyyyy','2025-10-21 15:10:27','2025-10-21 15:10:27'),(6,1,'ORD20251022000450619D8D',18297.00,'shipped','zzz, 13635990528, xxxxxxxxxx','zzz','13635990528','xxxxxxxxxx','2025-10-21 16:04:50','2025-10-21 16:06:22'),(7,1,'ORD20251022215653512BE5',288.00,'paid','zzx, 13635990528, xxxxxxxxxxx','zzx','13635990528','xxxxxxxxxxx','2025-10-22 13:56:53','2025-10-22 14:00:12');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = gbk */ ;
/*!50003 SET character_set_results = gbk */ ;
/*!50003 SET collation_connection  = gbk_chinese_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tr_orders_after_update` AFTER UPDATE ON `orders` FOR EACH ROW BEGIN
    IF OLD.status != 'cancelled' AND NEW.status = 'cancelled' THEN
        UPDATE products p
        INNER JOIN order_items oi ON p.id = oi.product_id
        SET p.stock = p.stock + oi.quantity,
            p.updated_at = CURRENT_TIMESTAMP
        WHERE oi.order_id = NEW.id;
        
        INSERT INTO inventory_logs (product_id, change_type, quantity, reason)
        SELECT oi.product_id, 'in', oi.quantity, CONCAT('Cancelled Order ID: ', NEW.id)
        FROM order_items oi
        WHERE oi.order_id = NEW.id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品图片',
  `status` enum('active','inactive') COLLATE utf8mb4_unicode_ci DEFAULT 'active' COMMENT '状态：上架/下架',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_name` (`name`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'iPhone 15 Pro','苹果最新旗舰手机',7999.00,99,1,'https://dummyimage.com/400x400/000000/ffffff&text=iPhone+15+Pro','active','2025-10-15 12:07:26','2025-10-21 16:04:50'),(2,'MacBook Air M2','苹果笔记本电脑',8999.00,49,1,'https://dummyimage.com/400x400/667eea/ffffff&text=MacBook+Air+M2','active','2025-10-15 12:07:26','2025-10-21 16:04:50'),(3,'Nike Air Max','经典运动鞋',899.00,200,2,'https://dummyimage.com/400x400/f56565/ffffff&text=Nike+Air+Max','active','2025-10-15 12:07:26','2025-10-21 15:14:39'),(4,'优衣库基础T恤','纯棉基础款T恤',99.00,500,2,'https://dummyimage.com/400x400/48bb78/ffffff&text=T-Shirt','active','2025-10-15 12:07:26','2025-10-21 15:14:39'),(5,'小米空气净化器','智能空气净化设备',1299.00,78,3,'https://dummyimage.com/400x400/ed8936/ffffff&text=Air+Purifier','active','2025-10-15 12:07:26','2025-10-21 16:04:50'),(6,'《Vue.js实战》','前端开发必读书籍',89.00,149,4,'https://dummyimage.com/400x400/4299e1/ffffff&text=Vue.js+Book','active','2025-10-15 12:07:26','2025-10-22 13:56:53'),(7,'瑜伽垫','防滑瑜伽练习垫',199.00,119,5,'https://dummyimage.com/400x400/9f7aea/ffffff&text=Yoga+Mat','active','2025-10-15 12:07:26','2025-10-22 13:56:53'),(8,'iPhone 17 Pro','手机',7999.00,10,1,'   https://dummyimage.com/400x400/000000/ffffff&text=iPhone+17+Pro','active','2025-10-22 01:40:41','2025-10-22 01:49:21'),(9,'ipad','平板',6999.00,80,1,'','active','2025-10-22 13:59:28','2025-10-22 13:59:41');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密密码',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER',
  `status` tinyint NOT NULL DEFAULT '1',
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@ecom.com',NULL,'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi','USER',1,'admin','2025-10-15 12:07:26','2025-10-15 12:07:26'),(2,'test@ecom.com',NULL,'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi','USER',1,'testuser','2025-10-15 12:07:26','2025-10-15 12:07:26'),(3,'483033504@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'zsj','2025-10-21 13:46:43','2025-10-21 13:46:43'),(4,'123456@qq.com',NULL,'8fbc04e7f281496e94d60d7709f9dec3','ADMIN',1,'oyh','2025-10-21 13:55:19','2025-10-21 13:55:19'),(5,'12345678@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'ppp','2025-10-21 15:59:01','2025-10-21 15:59:01'),(6,'1234567@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'zzz','2025-10-21 16:03:13','2025-10-21 16:03:13'),(7,'123456789@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'ooo','2025-10-22 04:57:39','2025-10-22 04:57:39'),(8,'794027@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'ooc','2025-10-22 13:53:44','2025-10-22 13:53:44'),(9,'1234567890@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','USER',1,'zzx','2025-10-22 13:55:01','2025-10-22 13:55:01'),(10,'987654321@qq.com',NULL,'21d294edf8433b5dbaefce449ec9bddf','ADMIN',1,'xxy','2025-10-22 13:57:53','2025-10-22 13:57:53');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_daily_sales`
--

DROP TABLE IF EXISTS `v_daily_sales`;
/*!50001 DROP VIEW IF EXISTS `v_daily_sales`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_daily_sales` AS SELECT 
 1 AS `sale_date`,
 1 AS `order_count`,
 1 AS `customer_count`,
 1 AS `total_sales`,
 1 AS `avg_order_value`,
 1 AS `product_variety`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_product_sales_stats`
--

DROP TABLE IF EXISTS `v_product_sales_stats`;
/*!50001 DROP VIEW IF EXISTS `v_product_sales_stats`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_product_sales_stats` AS SELECT 
 1 AS `product_id`,
 1 AS `product_name`,
 1 AS `current_price`,
 1 AS `total_sold_quantity`,
 1 AS `total_sales_amount`,
 1 AS `order_count`,
 1 AS `current_stock`,
 1 AS `stock_status`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_user_order_summary`
--

DROP TABLE IF EXISTS `v_user_order_summary`;
/*!50001 DROP VIEW IF EXISTS `v_user_order_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_user_order_summary` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `email`,
 1 AS `total_orders`,
 1 AS `total_spent`,
 1 AS `avg_order_amount`,
 1 AS `last_order_date`,
 1 AS `completed_orders`,
 1 AS `cancelled_orders`*/;
SET character_set_client = @saved_cs_client;

--
-- Current Database: `ecom_management`
--

USE `ecom_management`;

--
-- Final view structure for view `v_daily_sales`
--

/*!50001 DROP VIEW IF EXISTS `v_daily_sales`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = gbk */;
/*!50001 SET character_set_results     = gbk */;
/*!50001 SET collation_connection      = gbk_chinese_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_daily_sales` AS select cast(`o`.`created_at` as date) AS `sale_date`,count(distinct `o`.`id`) AS `order_count`,count(distinct `o`.`user_id`) AS `customer_count`,sum(`o`.`total_amount`) AS `total_sales`,avg(`o`.`total_amount`) AS `avg_order_value`,count(distinct `oi`.`product_id`) AS `product_variety` from (`orders` `o` left join `order_items` `oi` on((`o`.`id` = `oi`.`order_id`))) where (`o`.`status` <> 'cancelled') group by cast(`o`.`created_at` as date) order by `sale_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_product_sales_stats`
--

/*!50001 DROP VIEW IF EXISTS `v_product_sales_stats`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = gbk */;
/*!50001 SET character_set_results     = gbk */;
/*!50001 SET collation_connection      = gbk_chinese_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_product_sales_stats` AS select `p`.`id` AS `product_id`,`p`.`name` AS `product_name`,`p`.`price` AS `current_price`,coalesce(sum(`oi`.`quantity`),0) AS `total_sold_quantity`,coalesce(sum((`oi`.`quantity` * `oi`.`price`)),0) AS `total_sales_amount`,coalesce(count(distinct `oi`.`order_id`),0) AS `order_count`,`p`.`stock` AS `current_stock`,(case when (`p`.`stock` = 0) then 'Out of Stock' when (`p`.`stock` < 10) then 'Low Stock' else 'In Stock' end) AS `stock_status` from ((`products` `p` left join `order_items` `oi` on((`p`.`id` = `oi`.`product_id`))) left join `orders` `o` on(((`oi`.`order_id` = `o`.`id`) and (`o`.`status` <> 'cancelled')))) group by `p`.`id`,`p`.`name`,`p`.`price`,`p`.`stock` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_user_order_summary`
--

/*!50001 DROP VIEW IF EXISTS `v_user_order_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = gbk */;
/*!50001 SET character_set_results     = gbk */;
/*!50001 SET collation_connection      = gbk_chinese_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_user_order_summary` AS select `u`.`id` AS `user_id`,`u`.`username` AS `username`,`u`.`email` AS `email`,count(distinct `o`.`id`) AS `total_orders`,coalesce(sum((case when (`o`.`status` <> 'cancelled') then `o`.`total_amount` else 0 end)),0) AS `total_spent`,coalesce(avg((case when (`o`.`status` <> 'cancelled') then `o`.`total_amount` else NULL end)),0) AS `avg_order_amount`,max(`o`.`created_at`) AS `last_order_date`,count(distinct (case when (`o`.`status` = 'completed') then `o`.`id` end)) AS `completed_orders`,count(distinct (case when (`o`.`status` = 'cancelled') then `o`.`id` end)) AS `cancelled_orders` from (`users` `u` left join `orders` `o` on((`u`.`id` = `o`.`user_id`))) group by `u`.`id`,`u`.`username`,`u`.`email` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-22 22:02:27
