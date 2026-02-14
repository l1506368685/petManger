<template>
  <div class="home">
    <h2>欢迎使用宠物店管理系统</h2>
    <el-row :gutter="20" class="cards">
      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="card">
          <div class="num">{{ stats.petCount || 0 }}</div>
          <div class="label">宠物档案</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="card">
          <div class="num">{{ stats.orderCount || 0 }}</div>
          <div class="label">订单总数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="card">
          <div class="num">{{ stats.memberCount || 0 }}</div>
          <div class="label">会员总数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="card">
          <div class="num">{{ stats.totalIncome || 0 }}</div>
          <div class="label">总收入（元）</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="quick">
      <el-col :xs="12" :sm="12" :md="6"><el-button type="primary" @click="$router.push('/member')">会员管理</el-button></el-col>
      <el-col :xs="12" :sm="12" :md="6"><el-button type="success" @click="$router.push('/pet')">宠物档案</el-button></el-col>
      <el-col :xs="12" :sm="12" :md="6"><el-button type="warning" @click="$router.push('/order')">订单管理</el-button></el-col>
      <el-col :xs="12" :sm="12" :md="6"><el-button type="info" @click="$router.push('/goods')">商品管理</el-button></el-col>
    </el-row>
  </div>
</template>

<script>
import { getStatistics } from '@/api/home'

export default {
  name: 'Home',
  data() {
    return { stats: {} }
  },
  created() {
    getStatistics().then(res => { this.stats = res.data || {} })
  }
}
</script>

<style scoped>
.home { padding: 20px; }
.home h2 { margin-bottom: 24px; color: #333; font-size: 20px; }
.cards { margin-bottom: 24px; }
.card { text-align: center; }
.card .num { font-size: 28px; color: #409EFF; font-weight: bold; }
.card .label { color: #666; margin-top: 8px; }
.quick .el-col { text-align: center; }
.quick .el-button { width: 100%; }
@media (max-width: 768px) {
  .home { padding: 12px; }
  .home h2 { font-size: 18px; margin-bottom: 16px; }
  .card .num { font-size: 22px; }
  .cards .el-col { margin-bottom: 12px; }
  .quick .el-col { margin-bottom: 8px; }
}
</style>
