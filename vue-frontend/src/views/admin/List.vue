<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="账号"><el-input v-model="query.username" placeholder="账号" clearable /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="query.name" placeholder="姓名" clearable /></el-form-item>
        <el-form-item label="角色"><el-input v-model="query.role" placeholder="角色" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="role" label="角色" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template></el-table-column>
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '编辑', handler: openEdit }, { label: '密码重置', handler: openResetPwd }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '管理员'" :visible.sync="dialogVisible" width="500px" @close="form = {}">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="账号" prop="username"><el-input v-model="form.username" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="密码" :prop="form.id ? '' : 'password'"><el-input v-model="form.password" type="password" :placeholder="form.id ? '不填则不修改' : '请输入密码'" show-password /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="角色"><el-input v-model="form.role" placeholder="如：超级管理员" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="密码重置" :visible.sync="pwdVisible" width="400px">
      <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="100px">
        <el-form-item label="新密码" prop="newPassword"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword"><el-input v-model="pwdForm.confirmPassword" type="password" show-password /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="pwdVisible = false">取消</el-button><el-button type="primary" @click="submitPwd">确定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/admin'

export default {
  name: 'AdminList',
  data() {
    return {
      query: { current: 1, size: 10, username: '', name: '', role: '' },
      list: [], total: 0, loading: false,
      dialogVisible: false, pwdVisible: false,
      form: {}, pwdForm: { id: null, newPassword: '', confirmPassword: '' },
      formRules: { username: [{ required: true, message: '请输入账号', trigger: 'blur' }], password: [{ required: true, message: '请输入密码', trigger: 'blur' }] },
      pwdRules: {
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请再次输入密码', trigger: 'blur' }, { validator: (rule, value, cb) => { if (value !== this.pwdForm.newPassword) cb(new Error('两次密码不一致')); else cb(); }, trigger: 'blur' }]
      }
    }
  },
  created() { this.load() },
  methods: {
    load() { this.loading = true; api.pageList(this.query).then(res => { this.list = res.data.records || []; this.total = res.data.total || 0 }).finally(() => { this.loading = false }) },
    resetQuery() { this.query = { current: 1, size: 10, username: '', name: '', role: '' }; this.load() },
    openEdit(row) { this.form = row ? { ...row, password: '' } : { username: '', password: '', name: '', role: '超级管理员', status: 1 }; this.dialogVisible = true },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        if (this.form.id) { if (!this.form.password) delete this.form.password; api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() }) }
        else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() })
      })
    },
    openResetPwd(row) { this.pwdForm = { id: row.id, newPassword: '', confirmPassword: '' }; this.pwdVisible = true },
    submitPwd() { this.$refs.pwdForm.validate(valid => { if (!valid) return; api.resetPwd(this.pwdForm.id, this.pwdForm.newPassword).then(() => { this.$message.success('密码重置成功'); this.pwdVisible = false }) }) },
    handleDelete(row) { this.$confirm('确定删除该管理员？', '提示', { type: 'warning' }).then(() => api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })).catch(() => {}) }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
