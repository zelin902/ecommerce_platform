<template>
    <div class="cart-page">
      <!-- 返回首页按钮 -->
      <div class="back-home">
        <button @click="goBackHome">返回首页</button>
      </div>
  
      <!-- 购物车主体内容 -->
      <div class="cart-container">
        <h2 class="cart-title">我的购物车</h2>
        <!-- 配送地址展示 -->
        <div class="delivery-info">
          <label>配送地址：</label>
          <span>{{ deliveryAddress }}</span>
        </div>
  
        <!-- 购物车商品列表 -->
        <table class="cart-table">
          <thead>
            <tr>
              <th>选择</th>
              <th>商品信息</th>
              <th>单价</th>
              <th>数量</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item, index) in cartItems"
              :key="item.id"
            >
              <td>
                <input
                  type="checkbox"
                  v-model="item.checked"
                  @change="handleCheckAll"
                >
              </td>
              <td class="product-info">
                <img :src="item.firstImage" alt="商品图" class="product-img">
                <div class="product-desc">
                  <p>{{ item.name }}</p>
                  <p>{{ item.desc }}</p>
                </div>
              </td>
              <td>{{ item.price }} ¥</td>
              <td class="quantity-control">
                <button @click="decreaseQty(index)">-</button>
                <input
                  type="number"
                  v-model.number="item.quantity"
                  min="1"
                  @change="handleQtyChange(index)"
                >
                <button @click="increaseQty(index)">+</button>
              </td>
              <td>{{ item.stockStatus }}</td>
            </tr>
          </tbody>
        </table>
  
        <!-- 操作栏：全选、删除、总价、结算 -->
        <div class="cart-actions">
          <div class="select-all">
            <input
              type="checkbox"
              v-model="isAllChecked"
              @change="checkAll"
            >
            <label>全选</label>
            <button @click="deleteSelected">删除</button>
          </div>
          <div class="total-price">
            总价：{{ totalPrice }} ¥
          </div>
          <button class="checkout-btn" @click="handleCheckout">结算</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import JSONbig from 'json-bigint'
  export default {
    name: 'CartPage',
    data() {
      return {
        cartItems: [], // 购物车数据
        deliveryAddress: '辽宁省大连市榆林区大连理工大学', // 配送地址
        isAllChecked: false // 全选标识
      }
    },
    computed: {
      // 购物车商品数量（可用于导航栏徽标，若需要可暴露给父组件）
      cartCount() {
        return this.cartItems.reduce((total, item) => total + item.quantity, 0)
      },
      // 计算总价（已选商品）
      totalPrice() {
        return this.cartItems.reduce((total, item) => {
          if (item.checked) {
            return total + item.price * item.quantity
          }
          return total
        }, 0)
      }
    },
    methods: {
      // 返回首页
      goBackHome() {
        this.$router.push('/user') // 跳转到首页路由，根据实际路由调整
      },
      // 减少数量
      decreaseQty(index) {
        if (this.cartItems[index].quantity > 1) {
          this.cartItems[index].quantity--
        }
      },
      // 增加数量
      increaseQty(index) {
        this.cartItems[index].quantity++
      },
      // 数量改变时（可做合法性校验、同步接口等）
      handleQtyChange(index) {
        const qty = this.cartItems[index].quantity
        if (qty < 1) {
          this.cartItems[index].quantity = 1
        }
        // 这里可调用接口同步购物车数量：
        // this.updateCartItemQty(this.cartItems[index].id, qty)
      },
      // 单个商品选中
      handleCheckAll() {
        this.isAllChecked = this.cartItems.every(item => item.checked)
      },
      // 全选/全不选
      checkAll() {
        this.cartItems.forEach(item => {
          item.checked = this.isAllChecked
        })
      },
      // 删除选中商品
      deleteSelected() {
        this.cartItems = this.cartItems.filter(item => !item.checked)
        // 同时调用接口删除购物车数据：
        // const ids = this.cartItems.filter(item => item.checked).map(item => item.id)
        // this.deleteCartItems(ids)
      },
      // 结算（可跳转到结算页，或调用提交订单接口）
      handleCheckout() {
        const selectedItems = this.cartItems.filter(item => item.checked)
        if (selectedItems.length === 0) {
          this.$message.warning('请选择要结算的商品~')
          return
        }
        // 这里可跳转结算页，传递选中商品信息：
        this.$router.push({
          path: '/checkout',
          query: { items: JSON.stringify(selectedItems) }
        })
        // 或直接调用提交订单接口：
        // this.submitOrder(selectedItems)
      },
      // 模拟从接口拉取购物车数据（实际项目替换为真实接口）
      async loadCartItems() {
        try {
          const res = await fetch('http://localhost:8081/api/cart'); 
          const text = await res.text(); 
          const list = JSONbig.parse(text); 
          this.cartItems = list.map(item => ({
            ...item,
            id: String(item.id), 
            quantity: 1, 
            checked: false, 
            firstImage: item.images?.length ? item.images[0] : null, 
            stockStatus: '有余货' 
          }))
        } catch (e) {
          console.error('拉取购物车失败', e);
        }
      }
    },
    mounted() {
      this.loadCartItems() 
    }
  }
  </script>
  
  <style scoped>
  .cart-page {
    font-family: 'Arial', sans-serif;
    padding: 1rem;
  }
  
  /* 返回首页按钮样式 */
  .back-home {
    margin-bottom: 1rem;
  }
  .back-home button {
    padding: 0.5rem 1rem;
    background-color: #1890ff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .back-home button:hover {
    background-color: #40a9ff;
  }
  
  /* 购物车主体样式 */
  .cart-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 1rem;
  }
  .delivery-info {
    margin-bottom: 1rem;
    font-size: 1rem;
  }
  .cart-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 1rem;
  }
  .cart-table th,
  .cart-table td {
    border: 1px solid #ddd;
    padding: 0.8rem;
    text-align: center;
  }
  .product-info {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  .product-img {
    width: 60px;
    height: 60px;
    object-fit: cover;
  }
  .product-desc {
    text-align: left;
  }
  .quantity-control {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
  }
  .quantity-control input {
    width: 60px;
    text-align: center;
  }
  .cart-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 0;
  }
  .select-all {
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }
  .total-price {
    font-size: 1.2rem;
    font-weight: bold;
  }
  .checkout-btn {
    padding: 0.6rem 1.5rem;
    background-color: #ff4d4f;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 1rem;
  }
  .checkout-btn:hover {
    background-color: #ff7875;
  }
  
  @media (max-width: 768px) {
    .product-info {
      flex-direction: column;
      align-items: flex-start;
    }
    .cart-actions {
      flex-direction: column;
      gap: 1rem;
      align-items: flex-start;
    }
  }
  </style>