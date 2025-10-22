package com.example.spring_demo.controller;
import com.example.spring_demo.entity.CrudEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import com.example.spring_demo.entity.CrudEntity;
import com.example.spring_demo.repository.CrudRepository;


@RestController
public class CrudController {


    @Autowired
    private CrudRepository crudRepository;
//    findAll() → 取得所有資料
//    save() → 新增或更新資料
//    deleteById() → 刪除資料
//    findById() → 根據 ID 找資料


    // 用来接收来的前端的JSON 数据
    public static class ItemWrapper {
        private String itemName;
        private String itemDescription;

        public String getItemName() {
            return itemName;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
        //這裡命名跟前端的json命名要一致
        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }
    }

    //前端啟用時呼叫資料庫
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/getitems")
    public List<CrudEntity> getAllItems() {
        //findAll() 會從資料庫撈出所有該實體(Entity)對應的資料，回傳一個List。
        return crudRepository.findAll();
    }
//    測試有無撈到資料用
//    public List<CrudEntity> getAllItems() {
//        List<CrudEntity> list = crudRepository.findAll();
//        for (CrudEntity e : list) {
//            System.out.println("=== Item ===");
//            System.out.println("Name: " + e.getItemName());
//            System.out.println("Desc: " + e.getItemDescription());
//        }
//        return list;
//    }




    //實際業務流程API

    // 新增資料
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/receiveitem")

    public CrudEntity  receiveItem(@RequestBody CrudController.ItemWrapper itemWrapper) {
        // 印出所輸入值
        System.out.println("Received item: " + itemWrapper.getItemName());
        System.out.println("Received itemDescription: " + itemWrapper.getItemDescription());


        //建立Entity並儲存到資料庫
        CrudEntity crudEntity = new CrudEntity();
        crudEntity.setItemName(itemWrapper.getItemName());
        crudEntity.setItemDescription(itemWrapper.getItemDescription());
//        CrudEntity.setResult(result);

        //儲存到資料庫
        CrudEntity savedEntity = crudRepository.save(crudEntity);
//        CrudRepository.save(crudEntity);
        System.out.println("Saved entity with ID: " + savedEntity.getId());
        System.out.println("Saved entity with ItemName: " + savedEntity.getItemName());
        System.out.println("Saved entity with ItemName: " + savedEntity.getItemDescription());
        System.out.println("Item added successfully!");
        return savedEntity;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/deleteitem/{id}")
    public String deleteItem(@PathVariable Long id) {
        // 刪除邏輯：根據 id 刪除資料
        crudRepository.deleteById(id);
        return "Item deleted successfully!";
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/updateitem/{id}")
    public String updateItem(@PathVariable Long id, @RequestBody ItemWrapper itemWrapper) {
        // 根據 id 查找資料
        CrudEntity existingItem = crudRepository.findById(id).orElse(null);

        if (existingItem == null) {
            return "Item not found!";
        }

        // 更新資料
        existingItem.setItemName(itemWrapper.getItemName());
        existingItem.setItemDescription(itemWrapper.getItemDescription());

        // 儲存更新後的資料
        crudRepository.save(existingItem);

        return "Item updated successfully!";
    }




}


