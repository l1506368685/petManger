<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="商品名称"><el-input v-model="query.goodsName" placeholder="商品名称" clearable /></el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="query.alertType" placeholder="全部" clearable style="width:140px">
            <el-option label="采购预警" value="采购预警" /><el-option label="库存积压预警" value="库存积压预警" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <p style="margin-bottom:12px">库存低于下限时产生采购预警，高于上限时产生库存积压预警。请在商品管理中为商品设置库存上下限。</p>
      <el-table v-loading="loading" :data="list" border size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="goodsCode" label="商品编码" width="120" />
        <el-table-column prop="alertType" label="预警类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="scope.row.alertType === '采购预警' ? 'danger' : 'warning'" size="small">{{ scope.row.alertType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentStock" label="当前库存" width="100" align="right" />
        <el-table-column prop="stockLower" label="库存下限" width="100" align="right" />
        <el-table-column prop="stockUpper" label="库存上限" width="100" align="right" />
        <el-table-column prop="unit" label="单位" width="80" />
      </el-table>
      <p v-if="list.length === 0 && !loading" style="margin-top:12px;color:#909399">暂无预警记录。请为商品设置库存上下限后，系统将自动检测并在此展示。</p>
    </el-card>
    <el-card style="margin-top:16px">
      <h4 style="margin-top:0">即将到期批次（保质期预警）</h4>
      <el-form :inline="true" class="query-form">
        <el-form-item label="未来">
          <el-select v-model="expiryDays" style="width:100px" @change="loadExpiry">
            <el-option label="7 天内" :value="7" /><el-option label="15 天内" :value="15" /><el-option label="30 天内" :value="30" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" size="small" @click="loadExpiry">查询</el-button></el-form-item>
      </el-form>
      <el-table v-loading="expiryLoading" :data="expiryList" border size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="purchaseNo" label="采购单号" width="140" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="batchNo" label="批次号" width="100" />
        <el-table-column prop="quantity" label="数量" width="80" align="right" />
        <el-table-column prop="expiryDate" label="到期日" width="120" />
        <el-table-column prop="supplier" label="供应商" width="120" />
      </el-table>
      <p v-if="expiryList.length === 0 && !expiryLoading" style="margin-top:12px;color:#909399">暂无即将到期的批次。采购时可填写批次号、生产日期、到期日。</p>
    </el-card>
  </div>
</template>

<script>
import { alertList } from '@/api/inventory'
import { expiryAlertList } from '@/api/purchase'

export default {
  name: 'StockAlertList',
  data() {
    return {
      query: { goodsName: '', alertType: '' },
      list: [],
      loading: false,
      expiryDays: 30,
      expiryList: [],
      expiryLoading: false
    }
  },
  created() { this.load(); this.loadExpiry() },
  methods: {
    load() {
      this.loading = true
      alertList(this.query).then(res => { this.list = res.data || [] }).finally(() => { this.loading = false })
    },
    loadExpiry() {
      this.expiryLoading = true
      expiryAlertList({ days: this.expiryDays }).then(res => { this.expiryList = res.data || [] }).finally(() => { this.expiryLoading = false })
    },
    resetQuery() { this.query = { goodsName: '', alertType: '' }; this.load() }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
