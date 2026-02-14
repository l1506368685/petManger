<template>
  <div class="page">
    <el-card>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="dict-master">
            <div class="panel-title">字典类型（主表）</div>
            <el-button type="primary" size="small" style="margin-bottom:8px" @click="openTypeEdit()">新增类型</el-button>
            <el-table :data="typeList" border size="small" highlight-current-row @current-change="onTypeSelect">
              <el-table-column prop="dictType" label="类型编码" width="110" />
              <el-table-column prop="dictName" label="类型名称" />
              <el-table-column label="操作" :width="actionColumnWidth" fixed="right">
                <template slot-scope="scope">
                  <table-action-cell :row="scope.row" :actions="[{ label: '编辑', handler: openTypeEdit }, { label: '删除', handler: handleDeleteType, danger: true }]" />
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
        <el-col :span="16">
          <div class="dict-detail">
            <div class="panel-title">字典项（子表）{{ selectedType ? ' - ' + selectedType.dictName : '' }}</div>
            <template v-if="selectedType">
              <el-button type="primary" size="small" style="margin-bottom:8px" @click="openItemEdit()">新增字典项</el-button>
              <el-table :data="itemList" border size="small">
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="itemLabel" label="显示标签" />
                <el-table-column prop="itemValue" label="选项值" />
                <el-table-column prop="sort" label="排序" width="70" />
                <el-table-column prop="status" label="状态" width="80">
                  <template slot-scope="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template>
                </el-table-column>
                <el-table-column label="操作" :width="actionColumnWidth">
                  <template slot-scope="scope">
                    <table-action-cell :row="scope.row" :actions="[{ label: '编辑', handler: openItemEdit }, { label: '删除', handler: handleDeleteItem, danger: true }]" />
                  </template>
                </el-table-column>
              </el-table>
            </template>
            <div v-else class="tip">请先在左侧选择或新增一个字典类型</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <el-dialog :title="(typeForm.id ? '编辑' : '新增') + '字典类型'" :visible.sync="typeDialogVisible" width="480px">
      <el-form ref="typeFormRef" :model="typeForm" label-width="100px">
        <el-form-item label="类型编码" prop="dictType"><el-input v-model="typeForm.dictType" :disabled="!!typeForm.id" placeholder="如 pay_method" /></el-form-item>
        <el-form-item label="类型名称" prop="dictName"><el-input v-model="typeForm.dictName" placeholder="如 赔付方式" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="typeForm.sort" :min="0" style="width:100%" /></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="typeDialogVisible = false">取消</el-button><el-button type="primary" @click="submitType">确定</el-button></span>
    </el-dialog>
    <el-dialog :title="(itemForm.id ? '编辑' : '新增') + '字典项'" :visible.sync="itemDialogVisible" width="480px">
      <el-form ref="itemFormRef" :model="itemForm" label-width="100px">
        <el-form-item label="显示标签" prop="itemLabel"><el-input v-model="itemForm.itemLabel" /></el-form-item>
        <el-form-item label="选项值" prop="itemValue"><el-input v-model="itemForm.itemValue" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="itemForm.sort" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="itemForm.status"><el-radio :label="1">启用</el-radio><el-radio :label="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="itemDialogVisible = false">取消</el-button><el-button type="primary" @click="submitItem">确定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api/dict'

export default {
  name: 'DictList',
  data() {
    return {
      typeList: [],
      selectedType: null,
      itemList: [],
      typeDialogVisible: false,
      itemDialogVisible: false,
      typeForm: { dictType: '', dictName: '', sort: 0 },
      itemForm: { dictType: '', itemLabel: '', itemValue: '', sort: 0, status: 1 }
    }
  },
  created() { this.loadTypes() },
  methods: {
    loadTypes() { api.typeList().then(res => { this.typeList = res.data || []; this.selectedType = null; this.itemList = [] }) },
    onTypeSelect(row) { this.selectedType = row || null; if (row) this.loadItems(row.dictType) },
    loadItems(dictType) { api.items(dictType).then(res => { this.itemList = res.data || [] }) },
    openTypeEdit(row) {
      this.typeForm = row ? { ...row } : { dictType: '', dictName: '', sort: 0 }
      this.typeDialogVisible = true
    },
    submitType() {
      if (!this.typeForm.dictType || !this.typeForm.dictName) { this.$message.warning('请填写类型编码和名称'); return }
      if (this.typeForm.id) api.updateType(this.typeForm).then(() => { this.$message.success('修改成功'); this.typeDialogVisible = false; this.loadTypes() })
      else api.addType(this.typeForm).then(() => { this.$message.success('新增成功'); this.typeDialogVisible = false; this.loadTypes() })
    },
    handleDeleteType(row) {
      this.$confirm('删除类型会影响其下字典项，确定删除？', '提示', { type: 'warning' }).then(() => {
        api.deleteType(row.id).then(() => { this.$message.success('删除成功'); this.loadTypes() })
      }).catch(() => {})
    },
    openItemEdit(row) {
      if (!this.selectedType) { this.$message.warning('请先选择左侧字典类型'); return }
      this.itemForm = row ? { ...row } : { dictType: this.selectedType.dictType, itemLabel: '', itemValue: '', sort: 0, status: 1 }
      this.itemDialogVisible = true
    },
    submitItem() {
      if (!this.itemForm.itemLabel || !this.itemForm.itemValue) { this.$message.warning('请填写显示标签和选项值'); return }
      if (this.itemForm.id) api.updateItem(this.itemForm).then(() => { this.$message.success('修改成功'); this.itemDialogVisible = false; this.loadItems(this.selectedType.dictType) })
      else api.addItem(this.itemForm).then(() => { this.$message.success('新增成功'); this.itemDialogVisible = false; this.loadItems(this.selectedType.dictType) })
    },
    handleDeleteItem(row) {
      this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => {
        api.removeItem(row.id).then(() => { this.$message.success('删除成功'); this.loadItems(this.selectedType.dictType) })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.page { padding: 0; }
.dict-master, .dict-detail { border: 1px solid #ebeef5; border-radius: 4px; padding: 12px; min-height: 360px; }
.panel-title { font-weight: bold; margin-bottom: 12px; }
.tip { color: #909399; padding: 40px 0; text-align: center; }
</style>
