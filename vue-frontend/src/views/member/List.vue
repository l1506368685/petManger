<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="姓名"><el-input v-model="query.name" placeholder="姓名" clearable /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="query.phone" placeholder="手机号" clearable /></el-form-item>
        <el-form-item label="会员编号"><el-input v-model="query.memberNo" placeholder="会员编号" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="memberNo" label="会员编号" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="balance" label="当前余额" width="100" />
        <el-table-column prop="level" label="会员等级" />
        <el-table-column prop="createTime" label="注册时间" width="160" />
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top:12px"
        :current-page="query.current"
        :page-size="query.size"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '会员'" :visible.sync="dialogVisible" width="600px" @close="form = {}">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="会员编号" prop="memberNo"><el-input v-model="form.memberNo" placeholder="可留空自动生成" /></el-form-item>
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="当前余额"><el-input-number v-model="form.balance" :min="0" :precision="2" style="width:100%" placeholder="元" /></el-form-item>
        <el-form-item label="会员等级"><el-input v-model="form.level" placeholder="如：普通会员" /></el-form-item>
        <el-form-item label="性别"><el-input v-model="form.gender" /></el-form-item>
        <el-form-item label="生日"><el-date-picker v-model="form.birthday" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" type="textarea" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="会员详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="会员编号">{{ detail.memberNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ detail.name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="当前余额">{{ detail.balance }}</el-descriptions-item>
        <el-descriptions-item label="会员等级">{{ detail.level }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detail.gender }}</el-descriptions-item>
        <el-descriptions-item label="生日">{{ detail.birthday }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/member'

export default {
  name: 'MemberList',
  data() {
    return {
      query: { current: 1, size: 10, name: '', phone: '', memberNo: '' },
      list: [],
      total: 0,
      loading: false,
      dialogVisible: false,
      detailVisible: false,
      form: {},
      detail: {},
      formRules: { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }], phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }] }
    }
  },
  created() { this.load() },
  methods: {
    load() {
      this.loading = true
      api.pageList(this.query).then(res => {
        this.list = res.data.records || []
        this.total = res.data.total || 0
      }).finally(() => { this.loading = false })
    },
    resetQuery() { this.query = { current: 1, size: 10, name: '', phone: '', memberNo: '' }; this.load() },
    handlePageChange(p) { this.query.current = p; this.load() },
    openEdit(row) { this.form = row ? { ...row } : {}; this.dialogVisible = true },
    openDetail(row) { this.detail = row; api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() })
        else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() })
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该会员？', '提示', { type: 'warning' }).then(() => {
        api.remove(row.id).then(() => { this.$message.success('删除成功'); this.load() })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 12px; }
</style>
