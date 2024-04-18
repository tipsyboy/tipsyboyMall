<template>
  <CenterLayout>
    <h2>상품 수정</h2>
    <el-form :model="form" class="itemSaveForm" label-width="90px" status-icon>
      <el-form-item
        label="상품명"
        size="large"
        prop="itemName"
        :rules="[
          { required: true, message: '상품명을 입력하세요', trigger: 'blur' },
          { min: 3, max: 15, message: '3~15자의 상품명을 입력하세요.', trigger: 'blur' }
        ]"
      >
        <el-input v-model="form.itemName" placeholder="상품 제목 입력" />
      </el-form-item>
      <el-form-item
        label="가격"
        size="large"
        prop="price"
        :rules="[
          { required: true, message: '가격을 입력하세요.', trigger: 'blur' },
          { validator: validatePrice, trigger: 'blur' }
        ]"
      >
        <el-input v-model="form.price" type="number" placeholder="가격 입력" />
      </el-form-item>
      <el-form-item
        label="재고"
        size="large"
        prop="stock"
        :rules="[{ required: true, message: '재고를 입력하세요.', trigger: 'blur' }]"
      >
        <el-input-number v-model="form.stock" type="number" :min="1" :max="20" />
      </el-form-item>
      <el-form-item label="상품 설명" size="large">
        <el-input
          v-model="form.description"
          :rows="7"
          maxlength="200"
          type="textarea"
          placeholder="상품설명 적어"
          resize="none"
          show-word-limit
        />
      </el-form-item>
      <el-form-item size="large">
        <el-button @click="handleEditItem" type="primary">수정</el-button>
        <el-button @click="handleCancel">취소</el-button>
      </el-form-item>
    </el-form>
  </CenterLayout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const form = ref({
  itemId: 0,
  itemName: '',
  price: 0,
  stock: 0,
  description: ''
})

onMounted(() => {
  axios
    .get(`http://localhost:8080/items/${route.params.itemId}`, {
      withCredentials: true
    })
    .then((response) => {
      console.log(response.data)
      const itemInfo = response.data
      form.value.itemId = itemInfo.itemId
      form.value.itemName = itemInfo.itemName
      form.value.price = itemInfo.price
      form.value.stock = itemInfo.stock
      form.value.description = itemInfo.description
    })
    .catch((error) => {
      console.error(error)
    })
})

const validatePrice = (rule: any, value: any, callback: any) => {
  const price = parseInt(value, 10)
  if (isNaN(price) || price < 1000 || price > 2000000000) {
    callback(new Error('1000원 이상의 금액을 입력하세요.'))
  } else {
    callback()
  }
}

const handleEditItem = () => {
  axios
    .patch(
      `http://localhost:8080/items/${route.params.itemId}`,
      {
        itemName: form.value.itemName,
        price: form.value.price,
        stock: form.value.stock,
        description: form.value.description
      },
      {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )
    .then(() => {
      router.push({ name: 'itemDetail', params: { itemId: form.value.itemId } })
    })
    .catch((error) => {
      console.error(error)
    })
}

const handleCancel = () => {
  router.go(-1)
}
</script>

<style scoped></style>
