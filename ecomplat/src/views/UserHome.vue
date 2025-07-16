<template>
    <div class="user-home">
      <!-- 顶部欢迎栏 -->
      <div class="welcome-bar">
        <span class="welcome-text">欢迎回来，{{ username }}！</span>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
  
      <!-- 搜索栏 -->
      <div class="search-container">
        <div class="search-bar">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索商品..."
          >
          <button @click="handleSearch">搜索</button>
        </div>
      </div>
  
      <!-- 主导航栏 -->
      <nav class="main-nav">
        <div
          class="nav-item"
          @mouseenter="showCategoryDropdown = true"
          @mouseleave="showCategoryDropdown = false"
        >
          商品分类
          <div v-if="showCategoryDropdown" class="dropdown-menu">
            <div
              v-for="category in categories"
              :key="category.id"
              class="dropdown-item"
              @click="filterByCategory(category.id)"
            >
              {{ category.name }}
            </div>
          </div>
        </div>
        <div class="nav-item" @click="switchTab('orders')">我的订单</div>
        <div class="nav-item" @click="switchTab('user_profile')">个人中心</div>
        <div class="nav-item cart" @click="goToCart">
        购物车
        <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
      </div>
      </nav>
  
      <!-- 商品列表 -->
      <ProductList :products="filteredProducts" @add-to-cart="addToCart" />
    </div>
  </template>
  
  <script>
  import ProductList from '@/components/ProductList.vue'
  import JSONbig from 'json-bigint'

  export default {
    name: 'UserHome',
    components: { ProductList },
    data() {
      return {
        searchQuery: '',
        showCategoryDropdown: false,
        cartCount: 0,
        selectedCategory: null,
        username: localStorage.getItem('username') || '用户',
        categories: [
          { id: 1, name: '白酒' },
          { id: 2, name: '红酒' },
          { id: 3, name: '啤酒' },
          { id: 4, name: '洋酒' },
          { id: 5, name: '黄酒' },
          { id: 6, name: '果酒' },
          { id: 7, name: '保健酒' },
          { id: 8, name: '其他酒类' }
        ],
        products: [], // 原来是写死数组，现在为空
      }
    },
    computed: {
      filteredProducts() {
        let list = this.products
        if (this.selectedCategory) {
          list = list.filter(p => p.category === this.selectedCategory)
        }
        if (this.searchQuery) {
          list = list.filter(p => p.name.includes(this.searchQuery))
        }
        return list
      }
    },
    created() {
    // 页面加载时同步登录状态（核心修复）
    this.syncLoginState()
    // 加载商品数据（原有逻辑）
    this.loadRandomProducts()
   },
    methods: {
      syncLoginState() {
      const storedUsername = localStorage.getItem('username')
      const userId = localStorage.getItem('userId')
      if (storedUsername && userId) {
        // 已登录：显示用户名
        this.username = storedUsername
      } else {
        // 未登录：跳转回初始首页登录（容错处理）
        this.$message.warning('请先登录')
        this.$router.push('/') // 跳回EcommerceHome登录
      }
    },
      goToCart() {
      this.$router.push('/cart') // 跳转到购物车路由
    },
      handleSearch() {
        console.log('搜索:', this.searchQuery)
      },
      filterByCategory(categoryId) {
        this.selectedCategory = this.categories.find(c => c.id === categoryId).name
        this.showCategoryDropdown = false
      },
      switchTab(tab) {
        if (tab === 'user_profile') this.$router.push('/user_profile')
      },
      addToCart(product) {
        this.cartCount++
        console.log('添加到购物车:', product.name)
      },
      async loadRandomProducts() {
        try {
          const res = await fetch('http://localhost:8081/api/random?limit=5');
          const text = await res.text(); // 🔥先看原始字符串
          //console.log('原始响应文本:', text);
          const list = JSONbig.parse(text); // 🔥再解析 JSON
          //console.log('解析后第一个商品ID:', list[0].id, typeof list[0].id);
          this.products = list.map(p => {
          // 先保证 images 一定是数组
          let imgs = [];
          if (Array.isArray(p.images)) {
            imgs = p.images;
          } else if (typeof p.images === 'string' && p.images.trim().startsWith('[')) {
            try {
              imgs = JSON.parse(p.images);
            } catch { /* ignore */ }
          }
          return {
            ...p,
            id: String(p.id), // 🔥关键修复：防止精度丢失
            firstImage: imgs.length ? imgs[0] : null
          };
        });
      } catch (e) {
        console.error('拉取商品失败', e);
      }
    },
      logout() {
        localStorage.clear()
        this.$message?.success?.('已退出登录')
        this.$router.push('/')
      }
    },
    mounted() {
      this.syncLoginState(); 
    }
  }
  </script>
  
  <style scoped>
  .user-home {
    font-family: 'Arial', sans-serif;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }
  
  .welcome-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    background: #f5f5f5;
    border-bottom: 1px solid #eee;
  }
  .welcome-text {
    font-size: 16px;
    color: #333;
  }
  .logout-btn {
    padding: 6px 12px;
    background: #ff4d4f;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .logout-btn:hover {
    background: #ff7875;
  }
  
  .search-container {
    padding: 1rem 2rem;
    background-color: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  .search-bar {
    display: flex;
    max-width: 800px;
    margin: 0 auto;
  }
  .search-bar input {
    flex: 1;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px 0 0 4px;
    font-size: 1rem;
  }
  .search-bar button {
    padding: 0 1.5rem;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 0 4px 4px 0;
    cursor: pointer;
    font-size: 1rem;
  }
  
  .main-nav {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    padding: 15px 0;
    background-color: #1890ff;
    color: white;
    text-align: center;
  }
  .nav-item {
    position: relative;
    padding: 8px 0;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.2s;
  }
  .nav-item:hover {
    transform: translateY(-2px);
  }
  .dropdown-menu {
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    background-color: white;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    width: 150px;
    z-index: 100;
  }
  .dropdown-item {
    padding: 0.75rem 1rem;
    color: #333;
    border-bottom: 1px solid #eee;
  }
  .dropdown-item:hover {
    background-color: #f5f5f5;
  }
  .cart {
    position: relative;
  }
  .cart-badge {
    position: absolute;
    top: -5px;
    right: 10px;
    background-color: #f56c6c;
    color: white;
    border-radius: 50%;
    width: 18px;
    height: 18px;
    font-size: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  @media (max-width: 768px) {
    .main-nav {
      grid-template-columns: 1fr;
      gap: 10px;
    }
    .welcome-bar {
      padding: 10px;
      font-size: 14px;
    }
  }
  </style>