<script setup lang="ts">
import Navbar from '@/components/NavbarComp.vue'
import Footer from '@/components/FooterComp.vue'
import Content from '@/components/ContentComp.vue'
</script>

<template>
  <Navbar />
  <Content />
  <Footer />
</template>

<style>
body {
  margin: 0;
  width: 100%;
}

.el-table .cell {
  cursor: pointer;
}
</style>
