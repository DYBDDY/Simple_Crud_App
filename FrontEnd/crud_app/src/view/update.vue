<script setup>
import { ref, onMounted } from "vue";
import axios from "axios"; // 先引入 axios

// 表單資料
const itemName = ref("");
const itemDescription = ref("");

// 儲存項目的列表
const items = ref([]);

// 新增項目的處理函式
const handleSubmit = async (event) => {
  event.preventDefault(); // 阻止表單預設刷新頁面

  console.log("Item Name:", itemName.value);
  console.log("Item Description:", itemDescription.value);

  try {
    // 用 axios 送 POST 請求，資料會自動轉成 JSON
    const response = await axios.post("http://localhost:8080/receiveitem", {
      itemName: itemName.value,
      itemDescription: itemDescription.value,
    });

    console.log("Response from server:", response.data);
  } catch (error) {
    console.error("Axios error:", error);
  }

  // 把新項目加到列表中
  items.value.push({
    id: Date.now(),
    itemName: itemName.value,
    description: itemDescription.value,
  });

  // 清空輸入欄位
  itemName.value = "";
  itemDescription.value = "";
};

// 刪除項目函式
const deleteItem = async (id) => {
  // 先從列表中移除被刪除的項目
  items.value = items.value.filter((item) => item.id !== id);

  try {
    // 發送 DELETE 請求
    const response = await axios.delete(
      `http://localhost:8080/deleteitem/${id}`
    );
    console.log("Item deleted:", response.data);
  } catch (error) {
    console.error("Error deleting item:", error);
  }
};

//進入更新模式
const updateItem = (id) => {
  const editingItemId = ref(null);
  const itemToUpdate = items.value.find((item) => item.id === id);
  if(itemToUpdate){
    itemName.value = itemToUpdate.itemName;
    itemDescription.value = itemToUpdate.itemDescription;
    editingItemId.value = id;
  }
};

// 頁面啟動時（onMounted）去後端撈資料
onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:8080/getitems");
    items.value = response.data; // 把後端資料放進列表
  } catch (error) {
    console.error("Axios error on loading items:", error);
  }
});
</script>

<template>
  <div class="container">
    <h1>CRUD Example with Vue</h1>
    <!-- 表單部分 -->
    <!-- 新增模式 -->
    <form v-if="!isEditMode" @submit.prevent="handleSubmit">
      <h2>Add New Item</h2>
      <label for="itemName">Item Name:</label>
      <input
        type="text"
        id="itemName"
        v-model="itemName"
        placeholder="Enter item name"
        required
      />
      <br /><br />

      <label for="itemDescription">Item Description:</label>
      <input
        type="text"
        id="itemDescription"
        v-model="itemDescription"
        placeholder="Enter item description"
        required
      />
      <br /><br />

      <button type="submit">Add Item</button>
    </form>

    <!-- 編輯模式 -->
    <form v-else @submit.prevent="handleUpdate">
      <h2>Update Item</h2>

      <label for="editItemName">Item Name:</label>
      <input
        type="text"
        id="editItemName"
        v-model="itemName"
        placeholder="Update item name"
        required
      />
      <br /><br />

      <label for="editItemDescription">Item Description:</label>
      <input
        type="text"
        id="editItemDescription"
        v-model="itemDescription"
        placeholder="Update item description"
        required
      />
      <br /><br />

      <button type="submit">Update Item</button>
      <button type="button" @click="cancelEdit">Cancel</button>
    </form>

    <!-- 項目列表 -->
    <h2>Items List</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Description</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.itemName }}</td>
          <td>{{ item.itemDescription }}</td>
          <td>
            <button @click="deleteItem(item.id)">Delete</button>
            <!-- Edit 按鈕留空，日後可加 -->
            <button @click="updateItem(item.id)">Edit</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped></style>
