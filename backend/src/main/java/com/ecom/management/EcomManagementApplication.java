package com.ecom.management;

import com.ecom.management.entity.BackupRecord;
import com.ecom.management.entity.CartItem;
import com.ecom.management.entity.Category;
import com.ecom.management.entity.Product;
import com.ecom.management.entity.ProductSalesStats;
import com.ecom.management.mapper.CartItemMapper;
import com.ecom.management.mapper.CategoryMapper;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.mapper.ProductSalesStatsMapper;
import com.ecom.management.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EcomManagementApplication {

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ProductSalesStatsMapper productSalesStatsMapper;
    
    @Autowired
    private BackupService backupService;

    public static void main(String[] args) {
        SpringApplication.run(EcomManagementApplication.class, args);
    }

    // ==================== 商品分类管理 ====================
    
    @GetMapping("/products/categories")
    public Map<String, Object> getAllCategories() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categories = categoryMapper.findAll();
            response.put("success", true);
            response.put("message", "获取商品分类成功");
            response.put("data", categories);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取商品分类失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        return response;
    }

    @GetMapping("/products/category/{categoryId}")
    public Map<String, Object> getProductsByCategory(@PathVariable Long categoryId,
                                                      @RequestParam(defaultValue = "1") int pageNum,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        try {
            int offset = (pageNum - 1) * pageSize;
            List<Product> products = productMapper.searchProducts(categoryId, null, null, null, "created_at", "desc", offset, pageSize);
            int total = productMapper.countSearchProducts(categoryId, null, null, null);
            
            response.put("success", true);
            response.put("message", "获取分类商品成功");
            response.put("data", products);
            response.put("pageNum", pageNum);
            response.put("pageSize", pageSize);
            response.put("total", total);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取分类商品失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        return response;
    }

    // ==================== 商品搜索功能 ====================
    
    @GetMapping("/products/search")
    public Map<String, Object> searchProducts(@RequestParam(required = false) Long categoryId,
                                              @RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) BigDecimal minPrice,
                                              @RequestParam(required = false) BigDecimal maxPrice,
                                              @RequestParam(defaultValue = "created_at") String sortBy,
                                              @RequestParam(defaultValue = "desc") String sortOrder,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        try {
            int offset = (pageNum - 1) * pageSize;
            List<Product> products = productMapper.searchProducts(categoryId, keyword, minPrice, maxPrice, sortBy, sortOrder, offset, pageSize);
            int total = productMapper.countSearchProducts(categoryId, keyword, minPrice, maxPrice);

            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            data.put("total", total);
            data.put("pageNum", pageNum);
            data.put("pageSize", pageSize);

            response.put("success", true);
            response.put("message", "商品搜索成功");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "商品搜索失败: " + e.getMessage());
            response.put("data", new HashMap<>());
        }
        return response;
    }

    @GetMapping("/products/{productId}")
    public Map<String, Object> getProductById(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productMapper.findById(productId);
            if (product != null) {
                response.put("success", true);
                response.put("message", "获取商品详情成功");
                response.put("data", product);
            } else {
                response.put("success", false);
                response.put("message", "商品不存在");
                response.put("data", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取商品详情失败: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @GetMapping("/products/hot")
    public Map<String, Object> getHotProducts(@RequestParam(defaultValue = "5") int limit) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取最新的商品作为热门商品
            List<Product> products = productMapper.searchProducts(null, null, null, null, "created_at", "desc", 0, limit);
            response.put("success", true);
            response.put("message", "获取热门商品成功");
            response.put("data", products);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取热门商品失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        return response;
    }

    // ==================== 购物车管理 ====================
    
    @GetMapping("/cart")
    public Map<String, Object> getCartItems(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CartItem> cartItems = cartItemMapper.findByUserId(userId);
            List<Map<String, Object>> cartItemList = new ArrayList<>();
            
            for (CartItem item : cartItems) {
                Product product = productMapper.findById(item.getProductId());
                if (product != null) {
                    Map<String, Object> cartItemMap = new HashMap<>();
                    cartItemMap.put("id", item.getId());
                    cartItemMap.put("userId", item.getUserId());
                    cartItemMap.put("productId", product.getId());
                    cartItemMap.put("productName", product.getName());
                    cartItemMap.put("price", product.getPrice());
                    cartItemMap.put("quantity", item.getQuantity());
                    cartItemMap.put("imageUrl", product.getImageUrl());
                    cartItemMap.put("productStock", product.getStock());
                    cartItemMap.put("productStatus", product.getStatus());
                    cartItemMap.put("createdAt", item.getCreatedAt());
                    cartItemMap.put("updatedAt", item.getUpdatedAt());
                    cartItemList.add(cartItemMap);
                }
            }
            
            response.put("success", true);
            response.put("message", "获取购物车成功");
            response.put("data", cartItemList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取购物车失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        return response;
    }

    @PostMapping("/cart/add")
    public Map<String, Object> addToCart(@RequestParam Long userId, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            // 检查商品是否存在
            Product product = productMapper.findById(productId);
            if (product == null) {
                response.put("success", false);
                response.put("message", "商品不存在");
                return response;
            }
            
            // 检查库存
            if (product.getStock() < quantity) {
                response.put("success", false);
                response.put("message", "库存不足");
                return response;
            }
            
            // 检查购物车中是否已存在该商品
            List<CartItem> existingItems = cartItemMapper.findByUserId(userId);
            CartItem existingItem = null;
            for (CartItem item : existingItems) {
                if (item.getProductId().equals(productId)) {
                    existingItem = item;
                    break;
                }
            }
            
            if (existingItem != null) {
                // 如果已存在，更新数量
                int newQuantity = existingItem.getQuantity() + quantity;
                if (product.getStock() < newQuantity) {
                    response.put("success", false);
                    response.put("message", "库存不足");
                    return response;
                }
                cartItemMapper.updateQuantity(existingItem.getId(), newQuantity);
            } else {
                // 如果不存在，新增
                CartItem newItem = new CartItem();
                newItem.setUserId(userId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                cartItemMapper.insert(newItem);
            }
            
            response.put("success", true);
            response.put("message", "添加商品到购物车成功");
            
            // 返回更新后的购物车商品
            Map<String, Object> cartItemMap = new HashMap<>();
            cartItemMap.put("productId", product.getId());
            cartItemMap.put("productName", product.getName());
            cartItemMap.put("price", product.getPrice());
            cartItemMap.put("quantity", quantity);
            cartItemMap.put("imageUrl", product.getImageUrl());
            cartItemMap.put("productStock", product.getStock());
            response.put("data", cartItemMap);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "添加商品到购物车失败: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/cart/{cartItemId}")
    public Map<String, Object> updateCartItem(@RequestParam Long userId, 
                                               @PathVariable Long cartItemId, 
                                               @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            // 查询购物车商品
            List<CartItem> cartItems = cartItemMapper.findByUserId(userId);
            CartItem cartItem = null;
            for (CartItem item : cartItems) {
                if (item.getId().equals(cartItemId)) {
                    cartItem = item;
                    break;
                }
            }
            
            if (cartItem == null) {
                response.put("success", false);
                response.put("message", "购物车商品不存在");
                return response;
            }
            
            // 检查库存
            Product product = productMapper.findById(cartItem.getProductId());
            if (product == null) {
                response.put("success", false);
                response.put("message", "商品不存在");
                return response;
            }
            
            if (product.getStock() < quantity) {
                response.put("success", false);
                response.put("message", "库存不足");
                return response;
            }
            
            // 更新数量
            cartItemMapper.updateQuantity(cartItemId, quantity);
            
            response.put("success", true);
            response.put("message", "更新购物车商品数量成功");
            
            // 返回更新后的商品信息
            Map<String, Object> cartItemMap = new HashMap<>();
            cartItemMap.put("id", cartItemId);
            cartItemMap.put("productId", product.getId());
            cartItemMap.put("productName", product.getName());
            cartItemMap.put("price", product.getPrice());
            cartItemMap.put("quantity", quantity);
            cartItemMap.put("imageUrl", product.getImageUrl());
            cartItemMap.put("productStock", product.getStock());
            response.put("data", cartItemMap);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新购物车商品数量失败: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Map<String, Object> removeCartItem(@RequestParam Long userId, @PathVariable Long cartItemId) {
        Map<String, Object> response = new HashMap<>();
        try {
            int result = cartItemMapper.deleteById(cartItemId);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "删除购物车商品成功");
                response.put("cartItemId", cartItemId);
            } else {
                response.put("success", false);
                response.put("message", "购物车商品不存在");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除购物车商品失败: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping("/cart/clear")
    public Map<String, Object> clearCart(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            cartItemMapper.deleteByUserId(userId);
            response.put("success", true);
            response.put("message", "清空购物车成功");
            response.put("userId", userId);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清空购物车失败: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/cart/stats")
    public Map<String, Object> getCartStats(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CartItem> cartItems = cartItemMapper.findByUserId(userId);
            int totalItems = cartItems.size();
            int totalQuantity = 0;
            double totalAmount = 0.0;
            
            for (CartItem item : cartItems) {
                Product product = productMapper.findById(item.getProductId());
                if (product != null) {
                    totalQuantity += item.getQuantity();
                    totalAmount += product.getPrice().doubleValue() * item.getQuantity();
                }
            }
            
            response.put("success", true);
            response.put("message", "获取购物车统计成功");
            response.put("data", Map.of(
                "totalItems", totalItems,
                "totalQuantity", totalQuantity,
                "totalAmount", totalAmount,
                "userId", userId
            ));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取购物车统计失败: " + e.getMessage());
        }
        return response;
    }

    // ==================== 统计分析 ====================
    
    /**
     * 获取商品销量统计
     */
    @GetMapping("/stats/product-sales")
    public Map<String, Object> getProductSalesStats(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 分页查询
            int offset = (page - 1) * size;
            List<ProductSalesStats> statsList = productSalesStatsMapper.findSalesStatsByPage(offset, size);
            int total = productSalesStatsMapper.countSalesStats();
            
            Map<String, Object> data = new HashMap<>();
            data.put("records", statsList);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            data.put("totalPages", (int) Math.ceil((double) total / size));
            
            response.put("success", true);
            response.put("message", "获取商品销量统计成功");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取商品销量统计失败: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }
    
    /**
     * 获取所有商品销量统计（不分页）
     */
    @GetMapping("/stats/product-sales/all")
    public Map<String, Object> getAllProductSalesStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProductSalesStats> statsList = productSalesStatsMapper.findAllSalesStats();
            response.put("success", true);
            response.put("message", "获取商品销量统计成功");
            response.put("data", statsList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取商品销量统计失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
        }
        return response;
    }

    // ==================== 数据库备份 ====================
    
    /**
     * 执行数据库备份
     */
    @PostMapping("/backup/perform")
    public Map<String, Object> performBackup(@RequestBody(required = false) Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String description = request != null ? request.get("description") : "手动备份";
            Long backupId = backupService.performBackup(description);
            
            response.put("success", true);
            response.put("message", "数据库备份成功");
            response.put("data", Map.of("backupId", backupId));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "数据库备份失败: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }
    
    /**
     * 获取所有备份记录
     */
    @GetMapping("/backup/records")
    public Map<String, Object> getBackupRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BackupRecord> records = backupService.getBackupRecordsByPage(page, size);
            int total = backupService.countBackupRecords();
            
            Map<String, Object> data = new HashMap<>();
            data.put("records", records);
            data.put("total", total);
            data.put("page", page);
            data.put("size", size);
            data.put("totalPages", (int) Math.ceil((double) total / size));
            
            response.put("success", true);
            response.put("message", "获取备份记录成功");
            response.put("data", data);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取备份记录失败: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }
    
    /**
     * 删除备份记录
     */
    @DeleteMapping("/backup/records/{id}")
    public Map<String, Object> deleteBackupRecord(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = backupService.deleteBackupRecord(id);
            if (success) {
                response.put("success", true);
                response.put("message", "删除备份记录成功");
            } else {
                response.put("success", false);
                response.put("message", "备份记录不存在");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除备份记录失败: " + e.getMessage());
        }
        return response;
    }

    // ==================== 测试接口 ====================
    
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "🎉 电商管理系统API测试成功！");
        response.put("version", "1.0.0");
        response.put("modules", Arrays.asList("购物车管理", "商品分类和搜索"));
        response.put("timestamp", new Date());
        return response;
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "🎉 电商管理系统启动成功！");
        response.put("version", "1.0.0");
        response.put("modules", Arrays.asList("购物车管理", "商品分类和搜索"));
        response.put("apis", Arrays.asList(
            "GET /api/products/categories - 获取商品分类",
            "GET /api/products/category/{id} - 获取分类商品",
            "GET /api/products/search - 搜索商品",
            "GET /api/products/{id} - 获取商品详情",
            "GET /api/products/hot - 获取热门商品",
            "GET /api/cart?userId={id} - 获取购物车",
            "POST /api/cart/add?userId={id} - 添加商品到购物车",
            "PUT /api/cart/{id}?userId={id} - 更新购物车商品",
            "DELETE /api/cart/{id}?userId={id} - 删除购物车商品",
            "DELETE /api/cart/clear?userId={id} - 清空购物车",
            "GET /api/cart/stats?userId={id} - 获取购物车统计",
            "GET /api/test - 测试接口"
        ));
        return response;
    }
}