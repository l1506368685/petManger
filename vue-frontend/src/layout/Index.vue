<template>
  <el-container class="layout">
    <el-aside :class="['aside', { 'aside-mobile-hide': isMobile }]" width="220px">
      <div class="logo">宠物店管理</div>
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF">
        <el-menu-item index="/home"><i class="el-icon-s-home"></i><span>首页</span></el-menu-item>
        <el-submenu index="member">
          <template slot="title"><i class="el-icon-user"></i><span>会员中心</span></template>
          <el-menu-item index="/member">会员管理</el-menu-item>
          <el-menu-item index="/memberLifecycle">会员生命周期</el-menu-item>
          <el-menu-item index="/recharge">充值流水</el-menu-item>
          <el-menu-item index="/cardType">卡类型管理</el-menu-item>
          <el-menu-item index="/memberCard">会员卡</el-menu-item>
          <el-menu-item index="/cardTransaction">卡交易流水</el-menu-item>
        </el-submenu>
        <el-submenu index="pet">
          <template slot="title"><i class="el-icon-star-on"></i><span>宠物服务</span></template>
          <el-menu-item index="/petType">宠物类型</el-menu-item>
          <el-menu-item index="/pet">宠物档案</el-menu-item>
          <el-menu-item index="/vaccine">疫苗记录</el-menu-item>
          <el-menu-item index="/medical">医疗记录</el-menu-item>
        </el-submenu>
        <el-submenu index="goods">
          <template slot="title"><i class="el-icon-goods"></i><span>商品与采购</span></template>
          <el-menu-item index="/goods">商品管理</el-menu-item>
          <el-menu-item index="/supplier">供应商管理</el-menu-item>
          <el-menu-item index="/purchase">采购管理</el-menu-item>
        </el-submenu>
        <el-submenu index="inventory">
          <template slot="title"><i class="el-icon-box"></i><span>库存管理</span></template>
          <el-menu-item index="/inventory">库存管理</el-menu-item>
          <el-menu-item index="/stockAlert">库存预警</el-menu-item>
          <el-menu-item index="/stockCheck">库存盘点</el-menu-item>
          <el-menu-item index="/inventoryFlow">库存流水</el-menu-item>
        </el-submenu>
        <el-submenu index="sales">
          <template slot="title"><i class="el-icon-s-order"></i><span>销售与财务</span></template>
          <el-menu-item index="/order">订单管理</el-menu-item>
          <el-menu-item index="/claim">索赔记录</el-menu-item>
          <el-menu-item index="/finance">财务报表</el-menu-item>
        </el-submenu>
        <el-submenu index="system">
          <template slot="title"><i class="el-icon-setting"></i><span>系统设置</span></template>
          <el-menu-item index="/dict">数据字典</el-menu-item>
          <el-menu-item index="/admin">管理员管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container class="main-wrap">
      <el-header class="header">
        <el-button v-if="isMobile" type="text" class="menu-trigger" @click="drawerVisible = true">
          <i class="el-icon-s-fold"></i>
        </el-button>
        <span class="title">{{ $route.meta.title || '首页' }}</span>
        <div class="user">
          <span class="user-name">{{ user.name || user.role || '管理员' }}</span>
          <el-button type="text" size="small" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
    <el-drawer
      :visible.sync="drawerVisible"
      direction="ltr"
      size="260px"
      :with-header="false"
      custom-class="layout-drawer"
      @close="drawerVisible = false"
    >
      <div class="drawer-logo">宠物店管理</div>
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF" @select="drawerVisible = false">
        <el-menu-item index="/home"><i class="el-icon-s-home"></i><span>首页</span></el-menu-item>
        <el-submenu index="member">
          <template slot="title"><i class="el-icon-user"></i><span>会员中心</span></template>
          <el-menu-item index="/member">会员管理</el-menu-item>
          <el-menu-item index="/memberLifecycle">会员生命周期</el-menu-item>
          <el-menu-item index="/recharge">充值流水</el-menu-item>
          <el-menu-item index="/cardType">卡类型管理</el-menu-item>
          <el-menu-item index="/memberCard">会员卡</el-menu-item>
          <el-menu-item index="/cardTransaction">卡交易流水</el-menu-item>
        </el-submenu>
        <el-submenu index="pet">
          <template slot="title"><i class="el-icon-star-on"></i><span>宠物服务</span></template>
          <el-menu-item index="/petType">宠物类型</el-menu-item>
          <el-menu-item index="/pet">宠物档案</el-menu-item>
          <el-menu-item index="/vaccine">疫苗记录</el-menu-item>
          <el-menu-item index="/medical">医疗记录</el-menu-item>
        </el-submenu>
        <el-submenu index="goods">
          <template slot="title"><i class="el-icon-goods"></i><span>商品与采购</span></template>
          <el-menu-item index="/goods">商品管理</el-menu-item>
          <el-menu-item index="/supplier">供应商管理</el-menu-item>
          <el-menu-item index="/purchase">采购管理</el-menu-item>
        </el-submenu>
        <el-submenu index="inventory">
          <template slot="title"><i class="el-icon-box"></i><span>库存管理</span></template>
          <el-menu-item index="/inventory">库存管理</el-menu-item>
          <el-menu-item index="/stockAlert">库存预警</el-menu-item>
          <el-menu-item index="/stockCheck">库存盘点</el-menu-item>
          <el-menu-item index="/inventoryFlow">库存流水</el-menu-item>
        </el-submenu>
        <el-submenu index="sales">
          <template slot="title"><i class="el-icon-s-order"></i><span>销售与财务</span></template>
          <el-menu-item index="/order">订单管理</el-menu-item>
          <el-menu-item index="/claim">索赔记录</el-menu-item>
          <el-menu-item index="/finance">财务报表</el-menu-item>
        </el-submenu>
        <el-submenu index="system">
          <template slot="title"><i class="el-icon-setting"></i><span>系统设置</span></template>
          <el-menu-item index="/dict">数据字典</el-menu-item>
          <el-menu-item index="/admin">管理员管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-drawer>
  </el-container>
</template>

<script>
import { mapState } from 'vuex'

const MOBILE_BREAKPOINT = 768

export default {
  name: 'Layout',
  data() {
    return {
      isMobile: false,
      drawerVisible: false
    }
  },
  computed: mapState(['user']),
  mounted() {
    this.checkMobile()
    window.addEventListener('resize', this.checkMobile)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkMobile)
  },
  methods: {
    checkMobile() {
      this.isMobile = window.innerWidth < MOBILE_BREAKPOINT
      if (!this.isMobile) this.drawerVisible = false
    },
    logout() {
      this.$confirm('确定退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('logout')
        this.$router.push('/login')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.layout { height: 100%; }
.aside { background: #304156; transition: transform 0.2s, width 0.2s; }
.logo { height: 60px; line-height: 60px; text-align: center; color: #fff; font-size: 18px; font-weight: bold; }
.el-menu { border: none; }
.main-wrap { flex: 1; min-width: 0; display: flex; flex-direction: column; }
.header { background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; padding: 0 12px 0 8px; min-height: 50px; }
.menu-trigger { font-size: 22px; padding: 8px; margin-right: 4px; color: #303133; }
.title { font-size: 18px; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.user { display: flex; align-items: center; gap: 8px; flex-shrink: 0; }
.user-name { font-size: 13px; max-width: 80px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.main { background: #f0f2f5; padding: 12px; flex: 1; min-height: 0; overflow-x: auto; -webkit-overflow-scrolling: touch; }
.aside-mobile-hide { position: fixed; left: -220px; z-index: -1; width: 220px !important; }
.drawer-logo { height: 56px; line-height: 56px; text-align: center; color: #fff; font-size: 16px; font-weight: bold; background: #304156; }
</style>
<style>
.layout-drawer .el-drawer__body { padding: 0; overflow: auto; background: #304156; height: 100%; }
</style>
