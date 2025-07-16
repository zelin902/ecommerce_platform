<template>
    <div class="merchant-home">
      <!-- 左上角商家信息 & 退出 -->
      <div class="user-info">
        <span>欢迎，{{ merchantName }}</span>
        <span @click="logout" class="logout">退出</span>
      </div>
  
      <!-- 主导航栏（商家专属） -->
      <nav class="main-nav">
        <div class="nav-item" @click="switchTab('myGoods')">我的商品</div>
        <div class="nav-item" @click="$router.push('/merchant/add-goods')">上架商品</div>
        <div class="nav-item" @click="switchTab('orders')">交易记录</div>
        <div class="nav-item" @click="switchTab('profile')">个人中心</div>
      </nav>
  
      <!-- 内容区域：根据当前 tab 渲染不同组件 -->
      <section class="content">
        <!-- 这里可以换成真实组件或插槽，下面先用占位 -->
        <component :is="currentView" />
      </section>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  /* 当前高亮导航 */
  const activeTab = ref('myGoods')
  const switchTab = (tab) => { activeTab.value = tab }
  
  /* 根据 tab 动态渲染占位组件（示例） */
  const currentView = computed(() => {
    switch (activeTab.value) {
      case 'myGoods':  return 'MyGoods'
      case 'addGoods': return 'AddGoods'
      case 'orders':   return 'TradeRecords'
      case 'profile':  return 'MerchantProfile'
      default:         return 'MyGoods'
    }
  })
  
  /* 商家信息（登录后存 localStorage） */
  const merchantName = ref('')
  onMounted(() => {
    merchantName.value = localStorage.getItem('merchantName') || '商家'
  })
  
  /* 退出登录 */
  const logout = () => {
    localStorage.removeItem('uid')
    localStorage.removeItem('merchantName')
    router.replace({ name: 'Home' })
  }
  </script>
  
  <style scoped>
  .merchant-home {
    font-family: 'Arial', sans-serif;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }
  
  /* 左上角商家信息 */
  .user-info {
    position: absolute;
    top: 10px;
    left: 20px;
    display: flex;
    gap: 15px;
    font-size: 14px;
    z-index: 100;
  }
  .user-info span {
    color: #1890ff;
  }
  .logout {
    cursor: pointer;
    color: #f56c6c;
  }
  .logout:hover {
    text-decoration: underline;
  }
  
  /* 商家导航栏（4 栏） */
  .main-nav {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    padding: 15px 0;
    background-color: #1890ff;
    color: white;
    text-align: center;
  }
  .nav-item {
    padding: 8px 0;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.2s;
  }
  .nav-item:hover {
    transform: translateY(-2px);
  }
  
  /* 内容区 */
  .content {
    flex: 1;
    padding: 20px;
  }
  
  /* 响应式 */
  @media (max-width: 768px) {
    .main-nav {
      grid-template-columns: 1fr;
      gap: 10px;
    }
    .user-info {
      position: static;
      justify-content: center;
      margin-bottom: 10px;
      padding: 10px;
      background-color: #f5f5f5;
    }
  }
  </style>