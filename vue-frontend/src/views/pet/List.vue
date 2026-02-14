<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="宠物名称"><el-input v-model="query.petName" placeholder="宠物名称" clearable /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="query.contactName" placeholder="联系人" clearable /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="query.contactPhone" placeholder="联系电话" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="petName" label="宠物名称" />
        <el-table-column prop="typeName" label="宠物类型" />
        <el-table-column prop="breed" label="品种" />
        <el-table-column prop="color" label="毛色" />
        <el-table-column prop="contactName" label="联系人" />
        <el-table-column prop="contactPhone" label="联系电话" />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '宠物档案'" :visible.sync="dialogVisible" width="600px" @close="form = {}">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="宠物名称" prop="petName"><el-input v-model="form.petName" /></el-form-item>
        <el-form-item label="宠物类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择" style="width:100%" @change="onTypeChange">
            <el-option v-for="t in typeList" :key="t.id" :label="t.typeName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="品种"><el-input v-model="form.breed" /></el-form-item>
        <el-form-item label="毛色"><el-input v-model="form.color" /></el-form-item>
        <el-form-item label="性别"><el-input v-model="form.gender" /></el-form-item>
        <el-form-item label="出生日期"><el-date-picker v-model="form.birthday" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item>
        <el-form-item label="联系人" prop="contactName"><el-input v-model="form.contactName" /></el-form-item>
        <el-form-item label="联系电话" prop="contactPhone"><el-input v-model="form.contactPhone" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="宠物详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="宠物名称">{{ detail.petName }}</el-descriptions-item>
        <el-descriptions-item label="宠物类型">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="品种">{{ detail.breed }}</el-descriptions-item>
        <el-descriptions-item label="毛色">{{ detail.color }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/pet'
import { listAll as typeListAll } from '@/api/petType'

export default {
  name: 'PetList',
  data() {
    return {
      query: { current: 1, size: 10, petName: '', contactName: '', contactPhone: '' },
      list: [], total: 0, loading: false, typeList: [],
      dialogVisible: false, detailVisible: false, form: {}, detail: {},
      formRules: { petName: [{ required: true, message: '请输入宠物名称', trigger: 'blur' }], contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }] }
    }
  },
  created() { this.load(); typeListAll().then(res => { this.typeList = res.data || [] }) },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, petName: '', contactName: '', contactPhone: '' }; this.load() },
    onTypeChange(id) { const t = this.typeList.find(x => x.id === id); if (t) this.form.typeName = t.typeName },
    openEdit(row) { this.form = row ? { ...row } : {}; this.dialogVisible = true },
    openDetail(row) { api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() { this.$refs.formRef.validate(valid => { if (!valid) return; if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() }); else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() }) }) },
    handleDelete(row) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
