<template>
    <div class="contaier">
        <h1>CRUD Example with Vue</h1>
        <!-- 新增模式 -->
         <form @submit="handleSubmit">
         <h2>Add New Item</h2>
         <label for="itemName">Item Name:</label>
         <input type="text" id="itemName" v-model="itemName" placeholder="Enter item description" required/>
         <label for="itemDescription" >Item Description:</label>
         <input type="text" id="itemDescription"v-model="itemDescription" placeholder="Update item description" required/>
         <button id="additem">ADD ITEM</button>
         </form>
         
         <!-- 項目列表 -->
          <h2>Item List</h2>
          <table>
            <thead>
              <tr>
                <!-- <th>ID</th> -->
                <th>Name</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
                <tr v-for="item in items" >
                <!-- <td>{{item.id}}</td> -->
                <td>     
                  <input v-if="item.isEditing" v-model="item.itemName"/>
                  <span v-else>{{item.itemName}}</span>
                </td>
                <td>
                  <input v-if="item.isEditing" v-model="item.itemDescription"/>
                  <span v-else>{{item.itemDescription}}</span>
                </td>
                <td>
                  <button v-if="item.isEditing" @click="updateItem(item.id)">Update</button>   
                  <button v-else @click="editItem(item.id)">Edit</button>
                  <button v-if="item.isEditing" @click="cancelEdit(item.id)">Cancel</button>
                  <button v-else @click="deleteItem(item.id)">Delete</button>
                </td>
                <td>
                </td>
              </tr>
            </tbody>
            </table>
    </div>
</template>

<script setup>

import { ref, onMounted } from "vue";
import axios from "axios";

const itemName = ref("")
const itemDescription = ref("")
const items = ref([])
let currentId = 0;

//溫馨提醒items.value長這樣
// items.value = [
//   { id: 0, itemName: "Apple", itemDescription: "Fruit" },
//   { id: 1, itemName: "Banana", itemDescription: "Yellow fruit" }
// ]


//新增項目
const handleSubmit = async(e)=>{
  e.preventDefault();
  console.log(itemName.value)
  console.log(itemDescription.value)

  //前端頁面新增
  items.value.push({
    id:currentId++,
    itemName:itemName.value,
    itemDescription:itemDescription.value,
    isEditing: false,
    originalName: itemName.value,  
    originalDescription: itemDescription.value,
})


//送前端資料給後端
try{
    const response = await axios.post("http://localhost:8080/receiveitem",{
      itemName : itemName.value,
      itemDescription: itemDescription.value,
    })
    console.log("Response from server:", response.data);

    itemName.value = "";
    itemDescription.value = "";

    const getResponse = await axios.get("http://localhost:8080/getitems")
    items.value = getResponse.data
  }
  catch(error){
    console.log("error adding",error)
  }
}

//刪除項目
const deleteItem = async(id) => {
 let newItems =[];
 for(let i=0;i<items.value.length;i++){
  if(items.value[i].id===id){
    continue
  }else{
    newItems.push(items.value[i]);
  }
 }
 items.value=newItems

 //刪除資料庫資料
 try{
  const response = await axios.delete(`http://localhost:8080/deleteitem/${id}`)
  console.log("Item deleted:", response.data);
 }catch(error){
  console.log("error deleting",error)
 }
}


//開啟編輯
const editItem = (id)=>{
  for(let i =0;i<items.value.length;i++){
    if(items.value[i].id === id){  
        items.value[i].originalName = items.value[i].itemName;
        items.value[i].originalDescription = items.value[i].itemDescription;
        items.value[i].isEditing = true
        break
    }
  }
}

//取消編輯
const cancelEdit = (id) => {
  for (let i = 0; i < items.value.length; i++) {
    if (items.value[i].id === id) {
      // 取消編輯，恢復為非編輯模式
      items.value[i].itemName = items.value[i].originalName;
      items.value[i].itemDescription = items.value[i].originalDescription;
      items.value[i].isEditing = false;
      break;
    }
  }
}

//更新項目
const updateItem = async(id)=>{

  let updatedItem = null;

  //更新前端頁面資料
  for (let i = 0; i < items.value.length; i++) {
    if (items.value[i].id === id) {
      items.value[i].originalName = items.value[i].itemName;
      items.value[i].originalDescription = items.value[i].itemDescription;
      items.value[i].isEditing = false;

      updatedItem = items.value[i]
      break;
    }
  }

  //更新後端資料庫
  try{
    const response = await axios.put(`http://localhost:8080/updateitem/${id}`,{
      itemName: updatedItem.itemName,
      itemDescription: updatedItem.itemDescription
    })
    console.log("Response from server:", response.data);

    //每更新一次在讀取一次資料庫資料
    const getResponse = await axios.get("http://localhost:8080/getitems")
    items.value = getResponse.data
  }
  catch(error){
    console.log("error updating",error)
  }
}

//頁面啟動時（onMounted）去後端撈資料
onMounted(async()=>{
  try{
    const getResponse = await axios.get("http://localhost:8080/getitems")
    items.value = getResponse.data
  }
  catch(error){
    console.error("error on loading items:", error);
  }
})
</script>

<style scoped>
/* 整體容器 */
.container {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

/* 標題 */
h1 {
  color: #333;
  text-align: center;
}

h2 {
  color: #555;
  margin-top: 30px;
}

/* 表單區域 */
form {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

/* 標籤 */
label {
  display: block;
  margin-bottom: 5px;
  color: #333;
  font-weight: bold;
}

/* 輸入框 */
input[type="text"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

input[type="text"]:focus {
  outline: none;
  border-color: #4CAF50;
}

/* 按鈕 */
button {
  padding: 10px 20px;
  margin-right: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

/* 新增按鈕 */
#additem {
  background-color: #4CAF50;
  color: white;
  width: 100%;
}

#additem:hover {
  background-color: #45a049;
}

/* 編輯按鈕 */
button:nth-child(1) {
  background-color: #2196F3;
  color: white;
}

button:nth-child(1):hover {
  background-color: #0b7dda;
}

/* 刪除/取消按鈕 */
button:nth-child(2) {
  background-color: #f44336;
  color: white;
}

button:nth-child(2):hover {
  background-color: #da190b;
}

/* 表格 */
table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 表格標題 */
thead {
  background-color: #4CAF50;
  color: white;
}

th {
  padding: 12px;
  text-align: left;
}

/* 表格內容 */
td {
  padding: 12px;
  border-bottom: 1px solid #ddd;
}

tr:hover {
  background-color: #f5f5f5;
}

/* 表格內的輸入框 */
td input {
  width: 100%;
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>