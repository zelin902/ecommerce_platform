<template>
    <Teleport to="body">
      <div class="mask" @click.self="$emit('close')">
        <div class="modal">
          <h3>{{ editing ? '编辑地址' : '新建地址' }}</h3>
          <input v-model="form.name" placeholder="地址名称" />
          <input v-model="form.receiver" placeholder="收货人" />
          <input v-model="form.phone" placeholder="手机号" />
          <input v-model="form.detail" placeholder="详细地址" />
          <div class="btns">
            <button @click="$emit('close')">取消</button>
            <button @click="submit">保存</button>
          </div>
        </div>
      </div>
    </Teleport>
  </template>
  
  <script setup>
  import { reactive, watchEffect } from 'vue'
  
  const props = defineProps({ editing: Object })
  const emit = defineEmits(['close', 'save'])
  
  const form = reactive({
    id: null,
    name: '',
    receiver: '',
    phone: '',
    detail: '',
    isDefault: false,
    userId: localStorage.getItem('userId') || ''
  })
  
  watchEffect(() => {
    if (props.editing) Object.assign(form, props.editing)
  })
  
  function submit () {
    emit('save', { ...form })
  }
  </script>
  
  <style scoped>
  .mask {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.45);
    display: flex;
    align-items: center;
    justify-content: center;
    animation: fadeIn 0.3s;
  }
  
  @keyframes fadeIn {
    from { opacity: 0; }
    to   { opacity: 1; }
  }
  
  .modal {
    background: #fff;          /* 强制纯白 */
    width: 92vw;
    max-width: 420px;
    border-radius: 12px;
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
    padding: 32px 28px;
    display: flex;
    flex-direction: column;
    gap: 18px;
    animation: slideUp 0.35s;
  }
  
  @keyframes slideUp {
    from { transform: translateY(30px); opacity: 0; }
    to   { transform: translateY(0);    opacity: 1; }
  }
  
  h3 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    text-align: center;
    color: #1a1a1a;
  }
  
  input {
    padding: 12px 14px;
    font-size: 15px;
    border: 1px solid #e1e5e9;
    border-radius: 8px;
    background: #fafafa;
    transition: all 0.25s ease;
  }
  
  input:focus {
    border-color: #0066ff;
    background: #fff;
    outline: none;
    box-shadow: 0 0 0 2px rgba(0, 102, 255, 0.15);
  }
  
  .btns {
    display: flex;
    justify-content: flex-end;
    gap: 14px;
    margin-top: 8px;
  }
  
  .btns button {
    padding: 10px 22px;
    font-size: 15px;
    border-radius: 8px;
    border: none;
    cursor: pointer;
    transition: all 0.25s ease;
  }
  
  .btns button:first-child {
    background: #f2f2f2;
    color: #666;
  }
  
  .btns button:first-child:hover {
    background: #e5e5e5;
  }
  
  .btns button:last-child {
    background: #0066ff;
    color: #fff;
  }
  
  .btns button:last-child:hover {
    background: #0053d6;
  }
  
  @media (max-width: 480px) {
    .modal {
      padding: 24px 20px;
    }
    h3 {
      font-size: 18px;
    }
  }
  </style>