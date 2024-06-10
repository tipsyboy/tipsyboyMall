<template>
  <el-descriptions :column="2" :border="true" style="margin: 30px 0">
    <template #title>
      <h2>주문/배송 정보</h2>
    </template>
    <el-descriptions-item label="주문번호" width="25%">Y23712093</el-descriptions-item>
    <!-- <el-descriptions-item label="주문일자">{{ props.order.getFormattedDateTime() }}</el-descriptions-item> -->
    <el-descriptions-item label="주문하신 분">주문자</el-descriptions-item>
    <el-descriptions-item label="받으시는 분">받는사람</el-descriptions-item>
    <el-descriptions-item label="전화번호" :span="2">010-1234-5678</el-descriptions-item>
    <el-descriptions-item label="배송지 주소">
      <span> <el-tag size="small">도로명</el-tag>(13529) 경기 성남시 분당구 판교역로 166 (카카오 판교아지트) </span>
      <br />
      <span> <el-tag size="small">지번</el-tag>경기 성남시 분당구 백현동 532 </span>
    </el-descriptions-item>
  </el-descriptions>
</template>

<script setup lang="ts"></script>
