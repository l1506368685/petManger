<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="索赔编号"><el-input v-model="query.claimNo" placeholder="索赔编号" clearable /></el-form-item>
        <el-form-item label="宠物名称"><el-input v-model="query.petName" placeholder="宠物名称" clearable /></el-form-item>
        <el-form-item label="索赔原因"><el-input v-model="query.reason" placeholder="索赔原因" clearable /></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="query.startDate" type="date" value-format="yyyy-MM-dd" placeholder="开始日期" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="query.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束日期" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openAdd">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="claimNo" label="索赔编号" />
        <el-table-column prop="petName" label="宠物名称" />
        <el-table-column prop="eventDate" label="事件日期" width="120" />
        <el-table-column prop="claimAmount" label="索赔金额" width="100" />
        <el-table-column prop="finalAmount" label="最终索赔金额" width="120" />
        <el-table-column prop="payMethod" label="赔付方式" />
        <el-table-column prop="payTime" label="赔付时间" width="160" />
        <el-table-column label="操作" :width="actionColumnWidth">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(addForm.id ? '编辑' : '新增') + '索赔'" :visible.sync="addVisible" width="600px">
      <el-form ref="addForm" :model="addForm" label-width="120px">
        <el-form-item label="宠物">
          <el-select v-model="addForm.petId" filterable placeholder="请选择宠物" style="width:100%" @change="onPetChange">
            <el-option v-for="p in petList" :key="p.id" :label="p.petName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件日期"><el-date-picker v-model="addForm.eventDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item>
        <el-form-item label="索赔金额"><el-input-number v-model="addForm.claimAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="最终索赔金额"><el-input-number v-model="addForm.finalAmount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="赔付方式"><el-select v-model="addForm.payMethod" placeholder="请选择" style="width:100%"><el-option label="现金" value="现金" /><el-option label="转账" value="转账" /><el-option label="其他" value="其他" /></el-select></el-form-item>
        <el-form-item label="赔付时间"><el-date-picker v-model="addForm.payTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" /></el-form-item>
        <el-form-item label="索赔原因"><el-input v-model="addForm.reason" type="textarea" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="addForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="addVisible = false">取消</el-button><el-button type="primary" @click="submitAdd">确定</el-button></span>
    </el-dialog>
    <el-dialog title="索赔详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="索赔编号">{{ detail.claimNo }}</el-descriptions-item>
        <el-descriptions-item label="宠物名称">{{ detail.petName }}</el-descriptions-item>
        <el-descriptions-item label="事件日期">{{ detail.eventDate }}</el-descriptions-item>
        <el-descriptions-item label="索赔金额">{{ detail.claimAmount }}</el-descriptions-item>
        <el-descriptions-item label="最终索赔金额">{{ detail.finalAmount }}</el-descriptions-item>
        <el-descriptions-item label="赔付方式">{{ detail.payMethod }}</el-descriptions-item>
        <el-descriptions-item label="赔付时间">{{ detail.payTime }}</el-descriptions-item>
        <el-descriptions-item label="索赔原因">{{ detail.reason }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/claim'
import { listAll as petListAll } from '@/api/pet'

export default {
  name: 'ClaimList',
  data() {
    return {
      query: { current: 1, size: 10, claimNo: '', petName: '', reason: '', startDate: '', endDate: '' },
      list: [], total: 0, loading: false, petList: [],
      addVisible: false, detailVisible: false,
      addForm: {}, detail: {}
    }
  },
  created() { this.load(); petListAll().then(res => { this.petList = res.data || [] }) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, claimNo: '', petName: '', reason: '', startDate: '', endDate: '' }; this.load() },
    openAdd() { this.addForm = {}; this.addVisible = true },
    openEdit(row) { api.detail(row.id).then(res => { this.addForm = { ...res.data }; this.addVisible = true }) },
    onPetChange(id) { const p = this.petList.find(x => x.id === id); if (p) this.addForm.petName = p.petName },
    submitAdd() {
      if (!this.addForm.eventDate) this.addForm.eventDate = new Date().toISOString().slice(0, 10)
      if (!this.addForm.payTime) this.addForm.payTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
      if (this.addForm.id) api.update(this.addForm).then(() => { this.$message.success('修改成功'); this.addVisible = false; this.load() })
      else api.add(this.addForm).then(() => { this.$message.success('新增成功'); this.addVisible = false; this.load() })
    },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    handleDelete(row) { this.$confirm('确定删除该索赔记录？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
