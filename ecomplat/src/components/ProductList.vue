<template>
  <div class="product-list">
    <h2 class="section-title">热门商品</h2>
    <div class="products-container">
      <div
        v-for="product in products"
        :key="product.id"
        class="product-card"
        @click="goDetail(product.id)"
      >
        <div class="product-image">
          <img v-if="product.firstImage" :src="product.firstImage" alt="product" />
          <div v-else class="image-placeholder">暂无图片</div>
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-price">¥{{ product.price }}</p>
          <!-- 加入购物车按钮已移除 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProductList',
  props: {
    products: {
      type: Array,
      required: true
    }
  },
  methods: {
    goDetail(id) {
      if (!id) {
      alert('商品信息不完整');
      return;
    }
      this.$router.push(`/product/${id}`);
    }
  }
}
</script>
  
  <style scoped>
  /* 给卡片加可点击样式 */
.product-card {
  cursor: pointer;
}
  .product-list {
    padding: 2rem;
  }
  
  .section-title {
    margin-bottom: 1.5rem;
    color: #333;
  }
  
  .products-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 2rem;
  }
  
  .product-card {
    border: 1px solid #eee;
    border-radius: 8px;
    overflow: hidden;
    transition: transform 0.3s ease;
  }
  
  .product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  }
  
  .product-image {
    height: 200px;
    background-color: #f5f5f5;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .image-placeholder {
    color: #999;
  }
  
  .product-info {
    padding: 1rem;
  }
  
  .product-name {
    font-size: 1rem;
    margin-bottom: 0.5rem;
    height: 40px;
    overflow: hidden;
  }
  
  .product-price {
    color: #f56c6c;
    font-weight: bold;
    font-size: 1.2rem;
    margin: 0.5rem 0;
  }
  
  .add-to-cart {
    width: 100%;
    padding: 0.5rem;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .add-to-cart:hover {
    background-color: #096dd9;
  }
  </style>