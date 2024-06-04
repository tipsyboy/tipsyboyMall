<template>
  <CenterLayout>
    <h2>상품 수정</h2>
    <el-form :model="state.itemEditForm" :rules="formRules" label-width="90px" status-icon>
      <el-form-item label="상품 이미지" size="large">
        <el-upload action="#" v-model:file-list="state.imageFiles" list-type="picture-card" :auto-upload="false">
          <el-icon><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="상품명" size="large" prop="itemName">
        <el-input v-model="state.itemEditForm.itemName" placeholder="상품명을 입력하세요." />
      </el-form-item>

      <el-form-item label="가격" size="large" prop="price">
        <el-input v-model.number="state.itemEditForm.price" placeholder="상품의 판매 가격을 입력하세요." />
      </el-form-item>

      <el-form-item label="재고" size="large" prop="stock">
        <el-input-number
          v-model="state.itemEditForm.stock"
          placeholder="상품의 재고를 입력해주세요."
          :min="1"
          :max="20"
        />
      </el-form-item>

      <el-form-item label="상품 설명" size="large">
        <el-input
          v-model="state.itemEditForm.description"
          type="textarea"
          placeholder="상품 설명을 200자 내로 적어주세요."
          :maxlength="200"
          :rows="7"
          resize="none"
          show-word-limit
        />
      </el-form-item>

      <el-form-item size="large">
        <el-button type="primary" @click="editItem">수정</el-button>
        <el-button @click="router.back()">취소</el-button>
      </el-form-item>
    </el-form>
  </CenterLayout>
</template>

<script setup lang="ts">
import type Item from '@/entity/item/Item'
import ItemEditForm from '@/entity/item/ItemEditForm'
import ItemImage from '@/entity/item/ItemImage'
import ItemRepository from '@/repository/ItemRepository'
import { createFormRules } from '@/utils/ItemFormValidator'
import { ElMessage } from 'element-plus'
import { Plus, ZoomIn } from '@element-plus/icons-vue'
import { container } from 'tsyringe'
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import FilesForm from '@/entity/file/FilesForm'

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const props = defineProps<{
  itemId: number
}>()
const state = reactive({
  itemEditForm: new ItemEditForm(),
  imageFiles: [] as ItemImage[],
  newIamges: new FilesForm(),
})
const formRules = createFormRules()
const router = useRouter()

onMounted(() => {
  fetchItemDetails()
})

const fetchItemDetails = () => {
  ITEM_REPOSITORY.getItem(props.itemId)
    .then((item: Item) => {
      state.itemEditForm.itemName = item.itemName
      state.itemEditForm.price = item.price
      state.itemEditForm.stock = item.stock
      state.itemEditForm.description = item.description
      state.imageFiles = item.itemImages.map((image) => {
        return {
          storedName: image.storedName,
          url: getImageByFileName(image.storedName),
          status: 'finished',
        }
      })
    })
    .catch((e) => {
      console.error(e)
    })
}

const editItem = () => {
  ITEM_REPOSITORY.editItem(state.itemEditForm, props.itemId, state.imageFiles)
    .then(() => {
      ElMessage.success('상품 정보가 변경되었습니다.')
      router.push(`/item/${props.itemId}`)
    })
    .catch((e) => {
      console.error(e)
    })
}

const getImageByFileName = (storedName: string) => {
  return `/api/images/${storedName}`
}
</script>

<style scoped></style>
