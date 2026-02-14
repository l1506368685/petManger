const MOBILE_BREAKPOINT = 768

export default {
  data() {
    return {
      isMobile: false
    }
  },
  computed: {
    actionColumnWidth() {
      return this.isMobile ? 56 : 120
    }
  },
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
    }
  }
}
