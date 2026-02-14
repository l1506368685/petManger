<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="类型名称"><el-input v-model="query.typeName" placeholder="类型名称" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="id" label="类型ID" width="80" />
        <el-table-column prop="typeName" label="类型名称" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template></el-table-column>
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '宠物类型'" :visible.sync="dialogVisible" width="500px" @close="form = {}">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="类型名称" prop="typeName"><el-input v-model="form.typeName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="类型详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="类型ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="类型名称">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ detail.sort }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status === 1 ? '启用' : '禁用' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/petType'

export default {
  name: 'PetTypeList',
  data() {
    return {
      query: { current: 1, size: 10, typeName: '' },
      list: [], total: 0, loading: false,
      dialogVisible: false, detailVisible: false,
      form: {}, detail: {},
      formRules: { typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }] }
    }
  },
  created() { this.load() },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, typeName: '' }; this.load() },
    openEdit(row) { this.form = row ? { ...row } : { typeName: '', sort: 0, status: 1 }; this.dialogVisible = true },
    openDetail(row) { this.detail = row; api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() { this.$refs.formRef.validate(valid => { if (!valid) return; if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() }); else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() }) }) },
    handleDelete(row) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
