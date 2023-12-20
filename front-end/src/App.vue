<script setup lang="ts">
import { RouterLink } from 'vue-router'
import Navbar from '@/components/NavBarComp.vue'
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
</style>
