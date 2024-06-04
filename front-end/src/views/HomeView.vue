<template>
  <div class="main-container">
    <!-- main page header -->
    <div class="main-header">
      <h2 class="header__shop-name" style="color: white">Shop Name</h2>
      <div class="header__search-container">
        <el-input
          v-model="searchBar"
          class="search__input"
          size="large"
          placeholder="상품명을 입력하세요."
          :prefix-icon="Search"
        />
      </div>
    </div>

    <!-- main page content -->
    <div class="main-rep-items">
      <el-carousel :interval="5000" type="card" height="380px" indicator-position="outside" :pause-on-hover="true">
        <el-carousel-item v-for="(item, index) in items" :key="index">
          <div class="rep-items__content">
            <img class="rep-item__image" :src="item.image" alt="Product Image" />
            <div class="rep-item__info">
              <h3 class="rep-item__name">{{ item.name }}</h3>
              <div class="rep-item__price">{{ item.price }}</div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Search } from '@element-plus/icons-vue'
import { ref } from 'vue'

const searchBar = ref('')

const items = [
  {
    id: 1,
    name: 'Product 1',
    description: 'Description of Product 1',
    price: '$10.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 2,
    name: 'Product 2',
    description: 'Description of Product 2',
    price: '$20.49',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 3,
    name: 'Product 3',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 4,
    name: 'Product 4',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 5,
    name: 'Product 5',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 6,
    name: 'Product 6',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 7,
    name: 'Product 7',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 8,
    name: 'Product 8',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
  {
    id: 9,
    name: 'Product 9',
    description: 'Description of Product 3',
    price: '$15.99',
    image: 'https://via.placeholder.com/150',
  },
]
</script>

<style scoped>
/* main page header block*/
.main-header {
  background-color: black;
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.header__shop-name {
  display: flex;
  justify-content: center;
}

.header__search-container {
  width: 40vw;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  --el-border-radius-base: 25px;
}

.search__input {
  height: 50px;
  min-width: 100px;
}

/* main page rep-items block*/
.main-rep-items {
  margin-top: 50px;
}

.rep-items__content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 90%;
}

.rep-item__image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.rep-item__name {
  margin-bottom: 5px;
}
</style>
