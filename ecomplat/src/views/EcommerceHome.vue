<template>
    <div class="ecommerce-home">
      <!-- 顶部用户操作栏 -->
      <div class="user-actions">
        <span @click="showAuth('user', false)">用户注册</span>
        <span @click="showAuth('merchant', false)">商家入驻</span>
        <span @click="showAuth('user', true)">登录</span>
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
            >
              {{ category.name }}
            </div>
          </div>
        </div>
        <div class="nav-item" @click="switchTab('orders')">我的订单</div>
        <div class="nav-item" @click="switchTab('profile')">个人中心</div>
        <div class="nav-item cart" @click="switchTab('cart')">
          购物车
          <span v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</span>
        </div>
      </nav>
  
      <!-- 认证模态框 -->
      <AuthModal
        v-if="showAuthModal"
        v-model:authType="currentAuthType"
        :isLoginView="isLoginView"
        @close="showAuthModal = false"
        @submit="handleAuth"
      />
    </div>
  </template>
  
  <script>
  import AuthModal from '@/components/AuthModal.vue'
  import { register, login } from '@/api/auth'
  
  export default {
    name: 'EcommerceHome',
    components: { AuthModal },
    data() {
      return {
        searchQuery: '',
        showAuthModal: false,
        currentAuthType: 'user',
        isLoginView: false,
        showCategoryDropdown: false,
        cartCount: 0,
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
        products: [
          { id: 1, name: '茅台迎宾酒 普迎宾 2013年 酱香型白酒', price: 100 },
          { id: 2, name: '五粮液 52度 浓香型白酒', price: 200 },
          { id: 3, name: '泸州老窖 特曲 52度', price: 150 },
          { id: 4, name: '洋河蓝色经典 梦之蓝', price: 300 }
        ]
      }
    },
    computed: {
      filteredProducts() {
        return this.products.filter(p => p.name.includes(this.searchQuery))
      }
    },
    methods: {
      handleSearch() {
        console.log('搜索:', this.searchQuery)
      },
      switchTab(tab) {
        console.log('切换到:', tab)
      },
      showAuth(type, isLogin) {
        this.currentAuthType = type
        this.isLoginView = isLogin
        this.showAuthModal = true
      },
      async handleAuth(form) {
        try {
        const api = this.isLoginView ? login : register;
        const res = await api({
          username: form.username,
          email: form.email,
          phone: form.phone,
          password: form.password,
          type: form.type
      });

        if (this.isLoginView) {
          // 登录成功，保存用户 ID 和类型
          localStorage.setItem('userId', res.data.id);
          console.log(localStorage.getItem('userId'));
          localStorage.setItem('username', form.username);
          localStorage.setItem('userType', this.currentAuthType); // 'user' 或 'merchant'

          if (this.currentAuthType === 'merchant') {
            localStorage.setItem('merchantName', form.username);
            this.$message.success('商家登录成功');
            this.$router.push('/merchant'); // ✅ 跳转到商家首页
          } else {
            this.$message.success('用户登录成功');
            this.$router.push('/user'); // 或用户首页
          }
        } else {
          this.$message.success('注册成功，请登录');
        }

        this.showAuthModal = false;
      } catch (err) {
        const msg =
          err?.response?.data?.message ||
          err?.response?.data ||
          '请求失败，请稍后重试';
        alert(typeof msg === 'object' ? JSON.stringify(msg) : msg);
      }
    },
      addToCart(product) {
        this.cartCount++
        console.log('添加到购物车:', product.name)
      }
    }
  }
  </script>
  
  <style scoped>
  .ecommerce-home {
    font-family: 'Arial', sans-serif;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    position: relative;
  }
  
  /* 用户操作栏 - 左上角 */
  .user-actions {
    position: absolute;
    top: 10px;
    left: 20px;
    display: flex;
    gap: 15px;
    font-size: 14px;
    z-index: 100;
  }
  
  .user-actions span {
    color: #1890ff;
    cursor: pointer;
    transition: color 0.2s;
    padding: 5px;
  }
  
  .user-actions span:hover {
    color: #096dd9;
    text-decoration: underline;
  }
  
  .search-container {
    padding: 1rem 2rem;
    background-color: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-top: 40px; /* 为顶部用户操作栏留出空间 */
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
  
  /* 均匀分布的主导航栏 */
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
  
  .app-footer {
    text-align: center;
    padding: 1rem;
    background-color: #f5f5f5;
    margin-top: auto;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .main-nav {
      grid-template-columns: 1fr;
      gap: 10px;
    }
    
    .user-actions {
      position: static;
      justify-content: center;
      margin-bottom: 10px;
      padding: 10px;
      background-color: #f5f5f5;
    }
    
    .search-container {
      margin-top: 0;
    }
  }
  </style>