import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'), meta: { title: '登录' } },
  {
    path: '/',
    component: () => import('@/layout/Index.vue'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'Home', component: () => import('@/views/Home.vue'), meta: { title: '首页' } },
      { path: 'member', name: 'Member', component: () => import('@/views/member/List.vue'), meta: { title: '会员管理' } },
      { path: 'memberLifecycle', name: 'MemberLifecycle', component: () => import('@/views/memberLifecycle/Index.vue'), meta: { title: '会员生命周期管理' } },
      { path: 'recharge', name: 'Recharge', component: () => import('@/views/recharge/List.vue'), meta: { title: '充值流水' } },
      { path: 'cardType', name: 'CardType', component: () => import('@/views/cardType/List.vue'), meta: { title: '卡类型管理' } },
      { path: 'memberCard', name: 'MemberCard', component: () => import('@/views/memberCard/List.vue'), meta: { title: '会员储值卡/套餐卡' } },
      { path: 'cardTransaction', name: 'CardTransaction', component: () => import('@/views/cardTransaction/List.vue'), meta: { title: '卡交易流水' } },
      { path: 'petType', name: 'PetType', component: () => import('@/views/petType/List.vue'), meta: { title: '宠物类型' } },
      { path: 'pet', name: 'Pet', component: () => import('@/views/pet/List.vue'), meta: { title: '宠物档案' } },
      { path: 'vaccine', name: 'Vaccine', component: () => import('@/views/vaccine/List.vue'), meta: { title: '疫苗记录' } },
      { path: 'medical', name: 'Medical', component: () => import('@/views/medical/List.vue'), meta: { title: '医疗记录' } },
      { path: 'goods', name: 'Goods', component: () => import('@/views/goods/List.vue'), meta: { title: '商品管理' } },
      { path: 'supplier', name: 'Supplier', component: () => import('@/views/supplier/List.vue'), meta: { title: '供应商管理' } },
      { path: 'purchase', name: 'Purchase', component: () => import('@/views/purchase/List.vue'), meta: { title: '采购管理' } },
      { path: 'inventory', name: 'Inventory', component: () => import('@/views/inventory/List.vue'), meta: { title: '库存管理' } },
      { path: 'stockAlert', name: 'StockAlert', component: () => import('@/views/stockAlert/List.vue'), meta: { title: '库存预警' } },
      { path: 'stockCheck', name: 'StockCheck', component: () => import('@/views/stockCheck/List.vue'), meta: { title: '库存盘点' } },
      { path: 'inventoryFlow', name: 'InventoryFlow', component: () => import('@/views/inventoryFlow/List.vue'), meta: { title: '库存流水' } },
      { path: 'order', name: 'Order', component: () => import('@/views/order/List.vue'), meta: { title: '订单管理' } },
      { path: 'claim', name: 'Claim', component: () => import('@/views/claim/List.vue'), meta: { title: '索赔记录' } },
      { path: 'finance', name: 'Finance', component: () => import('@/views/finance/Report.vue'), meta: { title: '财务报表' } },
      { path: 'dict', name: 'Dict', component: () => import('@/views/dict/List.vue'), meta: { title: '数据字典' } },
      { path: 'admin', name: 'Admin', component: () => import('@/views/admin/List.vue'), meta: { title: '管理员管理' } }
    ]
  }
]

const router = new VueRouter({ mode: 'history', base: process.env.BASE_URL, routes })

router.beforeEach((to, from, next) => {
  document.title = (to.meta.title || '宠物店管理系统') + ' - 宠物店管理系统'
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
