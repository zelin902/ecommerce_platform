<template>
    <div class="product-detail">
      <!-- 顶部返回 -->
      <header class="detail-header">
        <span @click="$router.back()" class="back-btn">← 返回首页</span>
      </header>
  
    <section class="price-info">
    <!-- 左侧小图 -->
        <div class="left-thumb">
            <img :src="firstImage" alt="商品主图" />
        </div>

        <!-- 右侧信息 -->
        <div class="right-info">
            <h1 class="product-title">{{ product.name }}</h1>
            <p class="price">¥{{ product.price }}</p>

            <ul class="services">
                <li v-for="s in services" :key="s">{{ s }}</li>
            </ul>

            <p class="stock">余货量：{{ product.stock }}+</p>
            <el-tag type="primary" size="small">{{ product.category }}</el-tag>
              <!-- 新增：数量选择器 + 加入购物车 -->
            <div class="cart-bar">
                <div class="counter">
                <button @click="count > 1 ? count-- : 1">-</button>
                <span>{{ count }}</span>
                <button @click="count++">+</button>
                </div>
                <button class="add-cart-btn" @click="addCart">加入购物车</button>
            </div>

        </div>
    </section>
  
      <!-- 商品描述 -->
      <section class="description">
        <h2>商品描述</h2>
        <p>{{ product.description }}</p>
      </section>
  
      <!-- 动态规格 -->
      <section class="specs">
        <h2>商品详情</h2>
        <table>
          <tbody>
            <tr v-for="(value, key) in product.specs" :key="key">
              <td>{{ key }}</td>
              <td>{{ value }}</td>
            </tr>
          </tbody>
        </table>
      </section>
  
      <!-- 客户评价（示例） -->
      <section class="reviews">
        <h2>客户评价</h2>
        <div class="review-card" v-for="r in reviews" :key="r.user">
          <strong>{{ r.user }}</strong>
          <p>{{ r.comment }}</p>
        </div>
      </section>
  
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRoute } from 'vue-router'
  
  const route = useRoute()
  const product = ref({})
  const cartCount = ref(0);
  const reviews = ref([
    { user: '用户A', comment: '口感醇厚，余味悠长。' }
  ])
  const services = ref(['包邮', '名酒鉴真', '品质溯源', '免费上门退换'])
  
  // 商品主图
  const firstImage = computed(() =>
    Array.isArray(product.value.images) && product.value.images.length
      ? product.value.images[0]
      : ''
  )
  const count = ref(1)

  const addCart = async () => {
  try {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('请先登录');
      return;
    }
    if (!product.value.id) {
      alert('商品信息加载失败');
      return;
    }

    // 调用后端添加接口
    const response = await fetch(`/api/cart/add`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({
        userId: userId,
        productId: product.value.id,
        quantity: count.value.toString()
      })
    });

    if (response.ok) {
      // 成功：提示并更新购物车数量
      alert('加入购物车成功！');
      count.value = 1; // 重置数量选择器
      fetchCartCount(); // 刷新导航栏数量
    } else {
      const error = await response.text();
      alert('加入失败：' + error);
    }
  } catch (error) {
    console.error('添加失败：', error);
    alert('网络异常，请重试');
  }
};
  // 拉取商品详情（含 specs）
  onMounted(async () => {
    try {
        const id = String(route.params.id)
      const res = await fetch(`http://localhost:8081/api/product/${id}`)
      const text = await res.text()
      if (!text) throw new Error('空响应')
      product.value = JSON.parse(text)
    } catch (e) {
      console.error('获取商品失败', e)
    }
  })
  </script>
  
  <style scoped>
  .detail-header { padding: 12px; background: #fff; }
  .back-btn { cursor: pointer; font-size: 16px; color: #1890ff; }
  .price-info {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background: #fff;
}
.left-thumb img {
  width: 500px;
  height: 500px;
  object-fit: cover;
  border-radius: 4px;
  max-width: 100%;   /* 小屏时自适应 */
}
.right-info {
  flex: 1;
}
  .price { font-size: 24px; color: #f56c6c; margin: 8px 0; }
/* 服务标签水平排布 */
.services {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  list-style: none;
  padding: 0;
  margin: 8px 0;
}
.services li {
  background: #f1f1f1;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

/* 其余内容保持居中 */
.description,
.specs,
.reviews {

  /* ✅ 保留原来的 padding、背景、间距即可 */
  padding: 12px;
  margin-top: 12px;
  background: #fff;
}
  .specs table { width: 100%; border-collapse: collapse; }
  .specs td { padding: 6px 0; border-bottom: 1px solid #eee; }
  .detail-footer { position: fixed; bottom: 0; width: 100%; background: #fff; padding: 12px; box-shadow: 0 -2px 6px rgba(0,0,0,.1); }
  .cart-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 12px 0;
}

.counter {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}
.counter button {
  width: 24px;
  height: 24px;
  border: 1px solid #dcdcdc;
  background: #fff;
  cursor: pointer;
  border-radius: 2px;
}
.counter span {
  min-width: 24px;
  text-align: center;
}

.add-cart-btn {
  background: #ff5000;
  color: #fff;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
  </style>