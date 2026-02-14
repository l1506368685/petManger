module.exports = {
  devServer: {
    port: 3011,
    proxy: {
      '/api': {
        target: 'http://localhost:8011',
        changeOrigin: true
      }
    }
  }
}
