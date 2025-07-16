<template>
  <div class="add-goods">
    <h2>上架商品</h2>

    <el-form :model="form" label-width="120px" style="max-width: 680px">
      <!-- 商品名称 -->
      <el-form-item label="商品名称" required>
        <el-input v-model="form.name" placeholder="请输入商品名称" />
      </el-form-item>

      <!-- 提供服务 -->
      <el-form-item label="提供服务" required>
        <el-input v-model="form.service" placeholder="例如：上门维修、代排队等" />
      </el-form-item>

      <!-- 分类 -->
      <el-form-item label="商品分类" required>
        <el-select v-model="form.category" placeholder="请选择分类">
          <el-option label="服饰鞋包" value="clothes" />
          <el-option label="食品饮料" value="Food " />
          <el-option label="运动户外" value="Sports" />
          <el-option label="图书音像" value="Books" />
          <el-option label="3C 数码" value="Electronics" />
        </el-select>
      </el-form-item>

      <!-- 单价 -->
      <el-form-item label="单价（元）" required>
        <el-input-number v-model="form.price" :min="0" :precision="2" />
      </el-form-item>

      <!-- 库存 -->
      <el-form-item label="库存" required>
        <el-input-number v-model="form.stock" :min="0" />
      </el-form-item>

      <!-- 商品描述 -->
      <el-form-item label="商品描述">
        <el-input
          type="textarea"
          :rows="3"
          v-model="form.description"
          placeholder="简要描述商品亮点"
        />
      </el-form-item>

      <!-- 商品详情 key-value -->
      <el-form-item label="商品详情" required>
        <div
          v-for="(item, idx) in form.details"
          :key="idx"
          style="display:flex;gap:8px;margin-bottom:8px"
        >
          <el-input v-model="item.key" placeholder="属性名" style="width:140px" />
          <el-input v-model="item.value" placeholder="属性值" style="flex:1" />
          <el-button
            v-if="form.details.length > 1"
            type="danger"
            size="small"
            @click="removeDetail(idx)"
            >删</el-button
          >
        </div>
        <el-button
          v-if="form.details.length < 10"
          type="primary"
          size="small"
          @click="addDetail"
          >+ 添加属性</el-button
        >
      </el-form-item>

      <!-- 商品图片 -->
      <el-form-item label="商品图片">
        <el-upload
          ref="uploadRef"
          list-type="picture"
          :limit="5"
          :file-list="fileList"
          :before-upload="beforeUpload"
          :on-change="handleChange"
          :on-remove="handleRemove"
          :auto-upload="false"
          multiple
        >
          <el-button type="primary">选择图片（最多 5 张）</el-button>
        </el-upload>
      </el-form-item>

      <!-- 提交 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">上架商品</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { addGoods, uploadImage } from '@/api/goods'
import { useRouter } from 'vue-router'

const router = useRouter()

/* 表单数据 */
const form = reactive({
  name: '',
  service: '',
  category: '',
  price: 0,
  stock: 0,
  description: '',
  details: [{ key: '', value: '' }],
  images: []
})

/* 本地预览列表 */
const fileList = ref([])

/* 添加 / 删除详情 */
const addDetail = () => {
  if (form.details.length < 10) form.details.push({ key: '', value: '' })
}
const removeDetail = idx => form.details.splice(idx, 1)

/* 选图变化时同步 fileList，用于本地预览 */
const handleChange = (_, list) => {
  fileList.value = list
}

/* 删除 */
const handleRemove = (_, list) => {
  fileList.value = list
}

/* 文件格式/大小校验 */
const beforeUpload = (file) => {
  const isImg = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isImg) ElMessage.error('只能上传图片')
  if (!isLt2M) ElMessage.error('图片不能大于 2MB')
  return isImg && isLt2M
}

/* 提交：先批量上传图片，再提交整体数据 */
const submitForm = async () => {
  if (!form.name || !form.service || !form.category) {
    return ElMessage.error('请填写必填项')
  }
  
  const validDetails = form.details.filter(
    d => d.key.trim() && d.value.trim()
  )
  if (form.details.length && !validDetails.length) {
    return ElMessage.error('请至少填写一条有效商品详情')
  }
  
  const merchantId = Number(localStorage.getItem('uid')) // ✅ 取出商家ID
  if (!merchantId) {
    ElMessage.error('未登录，请先登录')
    return router.push('/')
  }

  try {
    // 1. 上传所有图片
    const uploadTasks = fileList.value.map(f =>
      uploadImage(f.raw).then(res => res.data.url)
    )
    const imageUrls = await Promise.all(uploadTasks)

    // 2. 提交完整数据
    const payload = {
      ...form,
      merchantId,
      images: imageUrls,
      details: validDetails
    }
    await addGoods(payload)
    ElMessage.success('商品上架成功')
    router.push('/goods')
  } catch (e) {
    ElMessage.error('上架失败')
  }
}
</script>

<style scoped>
.add-goods {
  padding: 20px;
}
</style>