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
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
                <tr v-for="item in items" >
                <td>{{item.id}}</td>
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
const handleSubmit =(e)=>{
  e.preventDefault();
  console.log(itemName.value)
  console.log(itemDescription.value)

  items.value.push({
    id:currentId++,
    itemName:itemName.value,
    itemDescription:itemDescription.value,
    isEditing: false,
    originalName: itemName.value,  
    originalDescription: itemDescription.value, 
})
}

//刪除項目
const deleteItem = (id) => {
 let newItems =[];
 for(let i=0;i<items.value.length;i++){
  if(items.value[i].id===id){
    continue
  }else{
    newItems.push(items.value[i]);
  }
 }
 items.value=newItems
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
const updateItem = (id)=>{
  alert("sss")
  for (let i = 0; i < items.value.length; i++) {
    if (items.value[i].id === id) {
      items.value.itemName = itemName.value;
      items.value.itemDescription = itemDescription.value;
      items.value[i].isEditing = false;
      break;
    }
  }

}
</script>