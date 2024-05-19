<template>
  <CenterLayout>
    <h2>상품 등록</h2>
    <el-form :model="state.itemCreateForm" :rules="formRules" label-width="90px" status-icon>
      <el-form-item label="상품 이미지" size="large">
        <el-upload
          action="#"
          v-model:file-list="state.imageFiles.imageFiles"
          list-type="picture-card"
          :auto-upload="false"
        >
          <el-icon><Plus /></el-icon>
          <!-- :on-preview="handlePictureCardPreview" -->
          <!-- :on-remove="handleRemove" -->
        </el-upload>
      </el-form-item>

      <el-form-item label="상품명" size="large" prop="itemName">
        <el-input v-model="state.itemCreateForm.itemName" placeholder="상품명을 입력하세요." />
      </el-form-item>

      <el-form-item label="가격" size="large" prop="price">
        <el-input
          v-model.number="state.itemCreateForm.price"
          placeholder="상품의 판매 가격을 입력하세요."
        />
      </el-form-item>

      <el-form-item label="재고" size="large" prop="stock">
        <el-input-number
          v-model="state.itemCreateForm.stock"
          placeholder="상품의 재고를 입력해주세요."
          :min="1"
          :max="20"
        />
      </el-form-item>

      <el-form-item label="상품 설명" size="large">
        <el-input
          v-model="state.itemCreateForm.description"
          type="textarea"
          placeholder="상품 설명을 200자 내로 적어주세요."
          :maxlength="200"
          :rows="7"
          resize="none"
          show-word-limit
        />
      </el-form-item>

      <el-form-item size="large">
        <el-button type="primary" @click="createItem()">등록</el-button>
      </el-form-item>
    </el-form>
  </CenterLayout>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage, type FormRules, type UploadProps, type UploadUserFile } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import ItemCreateForm from '@/entity/item/ItemCreateForm'
import FilesForm from '@/entity/file/FilesForm'
import ItemRepository from '@/repository/ItemRepository'
import { container } from 'tsyringe'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'

const router = useRouter()
const state = reactive({
  itemCreateForm: new ItemCreateForm(),
  imageFiles: new FilesForm()
})
const ITEM_REPOSITORY = container.resolve(ItemRepository)

const createItem = () => {
  ITEM_REPOSITORY.itemCreate(state.itemCreateForm, state.imageFiles)
    .then((response) => {
      console.log(response)
      ElMessage({ type: 'success', message: '상품 등록이 완료되었습니다.' })
      // router.replace('/')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}

const checkPrice = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('상품의 가격을 입력해주세요.'))
  }

  if (!Number.isInteger(value)) {
    callback(new Error('가격은 숫자로 입력해주세요.'))
  } else {
    if (value < 1000) {
      callback(new Error('가격은 1000원 보다 큰 값을 입력해주세요.'))
    } else {
      callback()
    }
  }
}

const formRules = reactive<FormRules<ItemCreateForm>>({
  itemName: [
    { required: true, message: '상품명을 입력해주세요.', trigger: 'blur' },
    { min: 2, max: 30, message: '상품명은 2~30자로 입력해주세요.', trigger: 'blur' }
  ],
  price: [{ required: true, validator: checkPrice, trigger: 'blur' }],
  stock: [{ required: true, message: '상품의 재고 수를 입력하세요.', trigger: 'blur' }]
})

// const handleRemove: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
//   console.log(uploadFile, uploadFiles)
// }

// const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
//   dialogImageUrl.value = uploadFile.url!
//   dialogVisible.value = true
// }
</script>

<style scoped></style>
