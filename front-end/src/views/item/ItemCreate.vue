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
        <el-input v-model.number="state.itemCreateForm.price" placeholder="상품의 판매 가격을 입력하세요." />
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
import { createFormRules } from '@/utils/ItemFormValidator'

const router = useRouter()
const state = reactive({
  itemCreateForm: new ItemCreateForm(),
  imageFiles: new FilesForm(),
})
const ITEM_REPOSITORY = container.resolve(ItemRepository)
const formRules = createFormRules()

const createItem = () => {
  ITEM_REPOSITORY.createItem(state.itemCreateForm, state.imageFiles)
    .then((itemId: number) => {
      ElMessage.success('상품 등록이 완료되었습니다.')
      router.push(`/item/${itemId}`)
    })
    .catch((e: HttpError) => {
      ElMessage.error(e.getMessage())
    })
}
</script>

<style scoped></style>
