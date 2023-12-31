<template>
  <ProfileMenu />
  <el-table style="width: 100%">
    <el-table-column prop="itemId" label="No" width="50" />
    <el-table-column label="카테고리">게시글 상태</el-table-column>
    <el-table-column prop="itemName" label="상품명" width="180"> </el-table-column>
    <el-table-column prop="status" label="상태" width="180" />
    <el-table-column :prop="'createdDate'" label="날짜"> </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import ProfileMenu from './ProfileMenu.vue'
</script>
