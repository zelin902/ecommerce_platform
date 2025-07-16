<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <h1>电商平台</h1>
    </header>
    
    <!-- 主内容区域 -->
    <main class="app-main">
      <router-view />
    </main>
    
    <!-- 底部导航 -->
    <footer class="app-footer">
      <p>© 2023 电商平台 版权所有</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif;
  line-height: 1.6;
  color: #333;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  background-color: #1890ff;
  color: white;
  padding: 1rem;
  text-align: center;
}

.app-header h1 {
  font-size: 1.5rem;
}

.app-main {
  flex: 1;
  padding: 1rem;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

.app-footer {
  background-color: #f5f5f5;
  padding: 1rem;
  text-align: center;
  font-size: 0.8rem;
  color: #666;
}

@media (max-width: 768px) {
  .app-main {
    padding: 0.5rem;
  }
}
</style>