<template>
  <div class="profile-wrap">
    <!-- 顶部栏 -->
    <header class="header">
      <span @click="$router.back()" class="back">← 返回首页</span>
      <h2>个人中心</h2>
    </header>

    <!-- 新增：导航栏 -->
    <nav class="nav-tabs">
      <span
        v-for="tab in tabs"
        :key="tab.key"
        :class="['tab', { active: activeTab === tab.key }]"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
      </span>
    </nav>

    <!-- 收货地址（原来整块内容） -->
    <section v-if="activeTab === 'address'" class="address-section">
      <!-- 当前默认地址提示 -->
      <section class="default-tip">
        当前默认地址：{{ defaultAddress?.name || '暂无' }}
      </section>

      <!-- 地址列表 -->
      <ul class="address-list">
        <li v-for="addr in addressList" :key="addr.id" class="address-card">
          <div class="info">
            <p><strong>{{ addr.receiver }}</strong> {{ addr.phone }}</p>
            <p>{{ addr.detail }}</p>
            <p class="tag">{{ addr.name }}</p>
          </div>
          <div class="actions">
            <button class="default-btn" @click="setDefault(addr.id)">
              {{ addr.isDefault ? '已默认' : '设为默认' }}
            </button>
            <button class="del-btn" @click="delAddress(addr.id)">删除</button>
          </div>
        </li>
      </ul>

      <!-- 新建 / 编辑弹窗 -->
      <AddressModal
        v-if="showModal"
        :editing="editingAddress"
        @close="showModal=false"
        @save="saveAddress"
      />

      <!-- 底部按钮 -->
      <footer class="add-footer">
        <button class="add-btn" @click="openAdd">新建地址</button>
      </footer>
    </section>

    <!-- 预留：个人信息、隐私内容占位 -->
    <section v-if="activeTab === 'profile'" class="placeholder">
      <p>个人信息模块待开发</p>
    </section>
    <section v-if="activeTab === 'privacy'" class="placeholder">
      <p>隐私内容模块待开发</p>
    </section>
  </div>
</template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import AddressModal from '@/components/AddressModal.vue'
  import { ElMessage } from 'element-plus'
  defineOptions({ name: 'UserProfile' })
  import {
  getAddress,
  saveAddress as apiSave,
  delAddress as apiDel,
  setDefault as apiSet
} from '@/api/address'

  // 从localStorage获取用户ID（假设登录时已存储）
  //console.log(localStorage.getItem('userId'))
  const userId = localStorage.getItem('userId') || ''

  // 地址列表
  const tabs = [
  { key: 'profile', label: '个人信息' },
  { key: 'address', label: '收货地址' },
  { key: 'privacy', label: '隐私内容' }
]
  const activeTab = ref('address')   // 默认显示收货地址
  const addressList = ref([])
  const defaultAddress = ref(null)
  const showModal = ref(false)
  const editingAddress = ref(null)
  
  // 拉取地址
// UserProfile.vue 中修改 loadAddress
async function loadAddress() {
  try {
    const list = await getAddress();
    console.log('获取到的地址列表:', list); // 打印获取到的地址数组
    // 验证返回数据是否为数组（防止后端返回非数组格式）
    if (Array.isArray(list)) {
      addressList.value = list;
      defaultAddress.value = list.find(i => i.isDefault) || null;
    } else {
      throw new Error('地址数据格式错误，应为数组');
    }
  } catch (err) {
    console.error('加载地址失败详情:', err); // 打印错误堆栈，定位问题
    ElMessage.error('加载地址失败');
  }
}
  
  // 设为默认
  async function setDefault (id) {
  try {
    await apiSet(id)
    ElMessage.success('已设为默认')
    loadAddress()
  } catch {
    ElMessage.error('设置失败')
  }
}
  
  // 删除
  async function delAddress (id) {
  try {
    await apiDel(id)
    ElMessage.success('已删除')
    loadAddress()
  } catch {
    ElMessage.error('删除失败')
  }
}
  
  // 打开新建
  function openAdd () {
    editingAddress.value = { userId }
    showModal.value = true
  }
  
  // 保存（新增或更新）
  async function saveAddress (form) {
  try {
    if (!form.userId) {
      form.userId = userId
    }
    await apiSave(form)
    ElMessage.success('保存成功')
    showModal.value = false
    loadAddress()
  } catch {
    ElMessage.error('保存失败')
  }
}
  
  onMounted(loadAddress)
  </script>
  
  <style scoped>
  .nav-tabs {
  display: flex;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.tab {
  flex: 1;
  text-align: center;
  padding: 12px 0;
  font-size: 15px;
  color: #333;
  cursor: pointer;
}
.tab.active {
  color: #1890ff;
  font-weight: bold;
  border-bottom: 2px solid #1890ff;
}
.placeholder {
  text-align: center;
  padding: 50px 0;
  color: #999;
}
  .profile-wrap { min-height: 100vh; background: #f6f6f6; }
  .header {
    display: flex;
    align-items: center;
    padding: 12px;
    background: #fff;
    border-bottom: 1px solid #eee;
  }
  .back { margin-right: 12px; font-size: 16px; color: #1890ff; cursor: pointer; }
  .default-tip { padding: 10px 15px; font-size: 14px; color: #666; }
  .address-list { padding: 0 15px 70px; }
  .address-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    margin-bottom: 10px;
    padding: 12px;
    border-radius: 6px;
  }
  .address-card .info p { margin: 4px 0; font-size: 14px; }
  .actions button {
    margin-left: 8px;
    padding: 4px 8px;
    border: none;
    border-radius: 4px;
    font-size: 12px;
    cursor: pointer;
  }
  .default-btn { background: #1890ff; color: #fff; }
  .del-btn { background: #ff4d4f; color: #fff; }
  .add-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0; /* 改为左右0，确保全屏宽度 */
  padding: 15px; /* 增加内边距，避免按钮边缘贴合屏幕 */
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05); /* 增加顶部阴影，区分内容区 */
}

/* 按钮样式修改（蓝色调、居中、修复边框问题） */
.add-btn {
  width: 90%; /* 宽度改为90%，避免边缘溢出 */
  margin: 0 auto; /* 水平居中 */
  padding: 14px 0; /* 垂直内边距调整，保持高度适中 */
  border: none;
  border-radius: 8px; /* 圆角增大，更美观 */
  background: #1890ff; /* 蓝色调（与导航栏活跃状态一致） */
  color: #fff;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  display: block; /* 确保margin:0 auto生效 */
  transition: background 0.3s; /* 增加hover过渡效果 */
}

/* 按钮hover效果 */
.add-btn:hover {
  background: #096dd9; /* 深色蓝，增强交互反馈 */
}
  </style>