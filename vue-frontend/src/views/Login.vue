<template>
  <div class="login-wrap">
    <div class="login-box">
      <h2>宠物店管理系统</h2>
      <el-form ref="form" :model="form" :rules="rules" label-width="0" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="账号" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" show-password @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <p class="tip">默认账号：admin / admin</p>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/admin'

export default {
  name: 'Login',
  data() {
    return {
      form: { username: 'admin', password: 'admin' },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.loading = true
        login(this.form).then(res => {
          this.$store.commit('setToken', res.data.token)
          this.$store.commit('setUser', res.data)
          this.$message.success('登录成功')
          this.$router.push('/')
        }).finally(() => { this.loading = false })
      })
    }
  }
}
</script>

<style scoped>
.login-wrap { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 16px; }
.login-box { width: 380px; max-width: 100%; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.15); }
.login-box h2 { text-align: center; margin-bottom: 30px; color: #333; font-size: 20px; }
.login-form { margin-top: 20px; }
.tip { text-align: center; color: #999; font-size: 12px; margin-top: 10px; }
@media (max-width: 768px) {
  .login-box { padding: 24px 20px; }
  .login-box h2 { font-size: 18px; margin-bottom: 20px; }
}
</style>
