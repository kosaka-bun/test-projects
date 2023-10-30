import { ElMessage } from 'element-plus'

const duration = 5000

const messageUtils = {
  message(str) {
    ElMessage({
      message: str,
      type: 'info',
      duration
    })
  },
  success(str) {
    ElMessage({
      message: str,
      type: 'success',
      duration
    })
  },
  warning(str) {
    ElMessage({
      message: str,
      type: 'warning',
      duration
    })
  },
  error(str) {
    ElMessage({
      message: str,
      type: 'error',
      duration
    })
  }
}

export default messageUtils