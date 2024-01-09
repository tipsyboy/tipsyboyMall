<template>
  <h2>상품 등록</h2>

  <el-form :model="form" class="itemSaveForm" label-width="90px" status-icon>
    <el-form-item label="상품 이미지" size="large">
      <el-upload
        v-model:fileList="fileList"
        action="#"
        list-type="picture-card"
        :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove"
        :auto-upload="false"
      >
        <el-icon><Plus /></el-icon>
      </el-upload>

      <el-dialog v-model="dialogVisible">
        <img w-full :src="dialogImageUrl" alt="Preview Image" />
      </el-dialog>
    </el-form-item>

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
      <el-button type="primary" @click="saveItem()">등록</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps, UploadUserFile } from 'element-plus'

const fileList = ref<UploadUserFile[]>([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const itemId = ref(-1)
const router = useRouter()
const form = ref({
  itemName: '',
  price: 0,
  stock: 0,
  description: ''
})

const saveItem = async () => {
  const formData = new FormData()
  // formData.append('itemName', form.value.itemName)
  // formData.append('price', `${form.value.price}`)
  // formData.append('stock', `${form.value.stock}`)
  // formData.append('description', form.value.description)

  console.log(fileList)
  formData.append(
    'itemCreateDto',
    new Blob([JSON.stringify(form.value)], { type: 'application/json' })
  )
  // fileList.value.forEach((file: any) => {
  //   formData.append('imageFiles', file)
  // })

  fileList.value.forEach((file: any) => {
    formData.append('imageFiles', file.raw)
  })

  console.log(formData)

  await axios
    .post('http://localhost:8080/items', formData, {
      withCredentials: true,
      headers: {
        // 'Content-Type': 'application/json'
        'Content-Type': 'multipart/form-data'
      }
    })
    .then((response) => {
      console.log(response)
      itemId.value = response.data
      router.replace({ name: 'itemDetail', params: { itemId: itemId.value } })
    })
    .catch((error) => {
      console.log(error)
    })

  console.log('Request Data:', formData)
}

const validatePrice = (rule: any, value: any, callback: any) => {
  const price = parseInt(value, 10)
  if (isNaN(price) || price < 1000 || price > 2000000000) {
    callback(new Error('1000원 이상의 금액을 입력하세요.'))
  } else {
    callback()
  }
}

const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles)
}

const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  dialogVisible.value = true
}
</script>

<style scoped></style>
