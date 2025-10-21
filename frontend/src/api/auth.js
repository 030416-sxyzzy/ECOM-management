import axios from '../utils/axios'

/**
 * 用户认证相关API
 */
export default {
  /**
   * 用户登录
   * @param {Object} data 登录信息 { email, password }
   * @returns {Promise}
   */
  login(data) {
    return axios.post('/auth/login', data)
  },
  
  /**
   * 用户注册
   * @param {Object} data 注册信息 { username, email, password, role }
   * @returns {Promise}
   */
  register(data) {
    return axios.post('/auth/register', data)
  },
  
  /**
   * 获取当前用户信息
   * @param {Number} userId 用户ID
   * @returns {Promise}
   */
  getCurrentUser(userId) {
    return axios.get('/auth/me', { params: { userId } })
  },
  
  /**
   * 用户退出登录
   * @returns {Promise}
   */
  logout() {
    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    return Promise.resolve()
  }
}