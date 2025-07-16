<template>
  <div class="modal">
    <div class="modal-content">
      <span class="close" @click="$emit('close')">&times;</span>

      <h2>{{ titleText }}</h2>

      <form @submit.prevent="handleSubmit">
        <!-- 登录：只显示用户名 -->
        <div v-if="isLoginView" class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" required />
        </div>

        <!-- 注册 / 入驻：三项全显 -->
        <template v-else>
          <div class="form-group">
            <label>用户名</label>
            <input v-model="form.username" type="text" required />
          </div>

          <div class="form-group">
            <label>手机号</label>
            <input v-model="form.phone" type="tel" maxlength="11" required />
          </div>

          <div class="form-group">
            <label>邮箱</label>
            <input v-model="form.email" type="email" required />
          </div>
        </template>

        <!-- 密码（通用） -->
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required />
        </div>

        <!-- 注册 / 入驻：确认密码 -->
        <div v-if="!isLoginView" class="form-group">
          <label>确认密码</label>
          <input v-model="form.confirmPassword" type="password" required />
        </div>

        <!-- 错误提示 -->
        <p v-if="errorMsg" class="error">{{ errorMsg }}</p>

        <button type="submit" :disabled="!!errorMsg">
          {{ isLoginView ? '登录' : '注册' }}
        </button>
      </form>

      <!-- 登录窗口下切换用户/商家 -->
      <div class="auth-switch" v-if="isLoginView">
        <span
          @click="$emit('update:authType', 'user')"
          :class="{ active: authType === 'user' }"
        >
          用户登录
        </span>
        <span> | </span>
        <span
          @click="$emit('update:authType', 'merchant')"
          :class="{ active: authType === 'merchant' }"
        >
          商家登录
        </span>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: 'AuthModal',
  props: {
    authType: { type: String, default: 'user' },
    isLoginView: { type: Boolean, default: true }
  },
  emits: ['close', 'submit', 'update:authType'],
  data() {
    return {
      form: {
        username: '',
        phone: '',
        email: '',
        password: '',
        confirmPassword: ''
      }
    };
  },
  computed: {
    titleText() {
      const who = this.authType === 'user' ? '用户' : '商家';
      return this.isLoginView ? `${who}登录` : (who === '商家' ? '商家入驻' : '用户注册');
    },
    errorMsg() {
      // 登录：仅校验用户名非空
      if (this.isLoginView && !this.form.username.trim()) {
        return '请输入用户名';
      }

      // 注册/入驻：手机号 & 确认密码
      if (!this.isLoginView) {
        if (this.form.phone && !/^1[3-9]\d{9}$/.test(this.form.phone)) {
          return '手机号格式不正确';
        }
        if (this.form.confirmPassword && this.form.password !== this.form.confirmPassword) {
          return '两次输入的密码不一致';
        }
      }
      return '';
    }
  },
  methods: {
    handleSubmit() {
      if (this.errorMsg) return;
      const typeMap = { user: 0, merchant: 1 };
      // 登录：只发 username、password、type
      // 注册：发全量字段
      this.$emit('submit', {
        ...this.form,
        type: typeMap[this.authType]
      });
    }
  }
};
</script>
  
  <style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background-color: white;
    padding: 2rem;
    border-radius: 8px;
    width: 400px;
    max-width: 90%;
    position: relative;
  }
  
  .close {
    position: absolute;
    top: 1rem;
    right: 1rem;
    font-size: 1.5rem;
    cursor: pointer;
  }
  
  .form-group {
    margin-bottom: 1rem;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 0.5rem;
  }
  
  .form-group input {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  button[type="submit"] {
    width: 100%;
    padding: 0.75rem;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-top: 1rem;
  }
  
  .auth-switch {
    text-align: center;
    margin-top: 1rem;
    color: #666;
  }
  
  .auth-switch span {
    cursor: pointer;
  }
  
  .auth-switch span.active {
    color: #1890ff;
    font-weight: bold;
  }
  
  .auth-switch span:not(:last-child) {
    margin-right: 0.5rem;
  }

  .error {
  color: #f56c6c;
  font-size: 0.85rem;
  margin: -0.5rem 0 0.5rem;
}
  </style>