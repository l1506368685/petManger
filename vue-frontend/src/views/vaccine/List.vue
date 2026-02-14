<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query">
        <el-form-item label="疫苗名称"><el-input v-model="query.vaccineName" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="petName" label="宠物名称" />
        <el-table-column prop="vaccineName" label="疫苗名称" />
        <el-table-column prop="vaccineDate" label="接种日期" />
        <el-table-column prop="doctor" label="接种医生" />
        <el-table-column prop="fee" label="费用" />
        <el-table-column label="操作" :width="actionColumnWidth">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="form.id ? '编辑' : '新增'" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="宠物">
          <el-select v-model="form.petId" filterable placeholder="请选择宠物" style="width:100%" @change="onPetChange">
            <el-option v-for="p in petList" :key="p.id" :label="p.petName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="疫苗名称"><el-input v-model="form.vaccineName" /></el-form-item>
        <el-form-item label="接种日期"><el-date-picker v-model="form.vaccineDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item>
        <el-form-item label="接种医生"><el-input v-model="form.doctor" /></el-form-item>
        <el-form-item label="费用"><el-input-number v-model="form.fee" :min="0" style="width:100%" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="宠物名称">{{ detail.petName }}</el-descriptions-item>
        <el-descriptions-item label="疫苗名称">{{ detail.vaccineName }}</el-descriptions-item>
        <el-descriptions-item label="接种日期">{{ detail.vaccineDate }}</el-descriptions-item>
        <el-descriptions-item label="费用">{{ detail.fee }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/vaccine'
import { listAll as petListAll } from '@/api/pet'

export default {
  name: 'VaccineList',
  data() {
    return {
      query: { current: 1, size: 10, vaccineName: '', petName: '', doctor: '' },
      list: [], total: 0, loading: false, petList: [],
      dialogVisible: false, detailVisible: false, form: {}, detail: {}
    }
  },
  created() { this.load(); petListAll().then(res => { this.petList = res.data || [] }) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    onPetChange(id) { const p = this.petList.find(x => x.id === id); if (p) this.form.petName = p.petName },
    openEdit(row) { this.form = row ? { ...row } : {}; this.dialogVisible = true },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() { if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() }); else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() }) },
    handleDelete(row) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
</style>
