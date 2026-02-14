<template>
  <div class="page">
    <el-card>
      <h3 style="margin-top:0;margin-bottom:12px">卡类型管理</h3>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="类型名称"><el-input v-model="query.typeName" placeholder="类型名称" clearable /></el-form-item>
        <el-form-item label="卡种">
          <el-select v-model="query.cardKind" placeholder="全部" clearable style="width:120px">
            <el-option label="储值卡" :value="1" />
            <el-option label="次卡" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">搜索</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-button type="primary" size="small" @click="openEdit()">新增卡类型</el-button>
      <el-table v-loading="loading" :data="list" border style="margin-top:12px" size="small">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="typeName" label="类型名称" />
        <el-table-column prop="cardKind" label="卡种" width="80">
          <template slot-scope="scope">{{ scope.row.cardKind === 2 ? '次卡' : '储值卡' }}</template>
        </el-table-column>
        <el-table-column prop="faceValue" label="面值" width="90" />
        <el-table-column prop="price" label="售价" width="90" />
        <el-table-column prop="validDays" label="有效天数" width="90" />
        <el-table-column prop="totalTimes" label="次卡次数" width="90" />
        <el-table-column prop="giftRule" label="赠送规则" />
        <el-table-column prop="status" label="状态" width="70">
          <template slot-scope="scope"><el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
          <template slot-scope="scope">
            <table-action-cell :row="scope.row" :actions="[{ label: '详情', handler: openDetail }, { label: '编辑', handler: openEdit }, { label: '删除', handler: handleDelete, danger: true }]" />
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px" :current-page="query.current" :page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="p => { query.current = p; load() }" />
    </el-card>
    <el-dialog :title="(form.id ? '编辑' : '新增') + '卡类型'" :visible.sync="dialogVisible" width="560px" @close="form = {}">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="类型名称" prop="typeName"><el-input v-model="form.typeName" placeholder="如：储值卡500" /></el-form-item>
        <el-form-item label="卡种" prop="cardKind">
          <el-radio-group v-model="form.cardKind">
            <el-radio :label="1">储值卡</el-radio>
            <el-radio :label="2">次卡</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="面值" prop="faceValue"><el-input-number v-model="form.faceValue" :min="0" :precision="2" style="width:100%" placeholder="储值卡面值/次卡单次价值" /></el-form-item>
        <el-form-item label="售价" prop="price"><el-input-number v-model="form.price" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="有效天数" prop="validDays"><el-input-number v-model="form.validDays" :min="1" style="width:100%" placeholder="365" /></el-form-item>
        <el-form-item label="次卡总次数" prop="totalTimes" v-if="form.cardKind === 2"><el-input-number v-model="form.totalTimes" :min="1" style="width:100%" placeholder="如10" /></el-form-item>
        <el-form-item label="赠送规则" prop="giftRule"><el-input v-model="form.giftRule" placeholder="如：充500送50" /></el-form-item>
        <el-form-item label="可绑副卡数" prop="maxSubCards"><el-input-number v-model="form.maxSubCards" :min="0" style="width:100%" placeholder="0为不可绑定" /></el-form-item>
        <el-form-item label="排序" prop="sort"><el-input-number v-model="form.sort" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></span>
    </el-dialog>
    <el-dialog title="卡类型详情" :visible.sync="detailVisible" width="500px">
      <el-descriptions :column="1" border v-if="detail.id">
        <el-descriptions-item label="类型名称">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="卡种">{{ detail.cardKind === 2 ? '次卡' : '储值卡' }}</el-descriptions-item>
        <el-descriptions-item label="面值">{{ detail.faceValue }}</el-descriptions-item>
        <el-descriptions-item label="售价">{{ detail.price }}</el-descriptions-item>
        <el-descriptions-item label="有效天数">{{ detail.validDays }}</el-descriptions-item>
        <el-descriptions-item label="次卡总次数">{{ detail.totalTimes }}</el-descriptions-item>
        <el-descriptions-item label="赠送规则">{{ detail.giftRule }}</el-descriptions-item>
        <el-descriptions-item label="可绑副卡数">{{ detail.maxSubCards }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ detail.status === 1 ? '启用' : '禁用' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/cardType'

export default {
  name: 'CardTypeList',
  data() {
    return {
      query: { current: 1, size: 10, typeName: '', cardKind: null, status: null },
      list: [], total: 0, loading: false,
      dialogVisible: false, detailVisible: false,
      form: {},
      detail: {},
      formRules: {
        typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }],
        cardKind: [{ required: true, message: '请选择卡种', trigger: 'change' }],
        price: [{ required: true, message: '请输入售价', trigger: 'blur' }]
      }
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
    resetQuery() { this.query = { current: 1, size: 10, typeName: '', cardKind: null, status: null }; this.load() },
    openEdit(row) {
      this.form = row ? { ...row } : { cardKind: 1, validDays: 365, totalTimes: 0, maxSubCards: 0, sort: 0, status: 1 }
      this.dialogVisible = true
    },
    openDetail(row) { this.detail = row; api.detail(row.id).then(res => { this.detail = res.data; this.detailVisible = true }) },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        if (this.form.id) api.update(this.form).then(() => { this.$message.success('修改成功'); this.dialogVisible = false; this.load() })
        else api.add(this.form).then(() => { this.$message.success('新增成功'); this.dialogVisible = false; this.load() })
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该卡类型？', '提示', { type: 'warning' }).then(() => {
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
