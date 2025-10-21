import axios from '../utils/axios'

/**
 * 用户认证相关API
 */
export default {
  /**
   * 用户账号密码登录
   * @param {Object} data 登录信息
   * @returns {Promise}
   */
  login(data) {
    return axios.post('/auth/login', data)
  },
  
  /**
   * 手机验证码登录
   * @param {Object} data 手机登录信息
   * @returns {Promise}
   */
  phoneLogin(data) {
    return axios.post('/auth/phone-login', data)
  },
  
  /**
   * 用户注册
   * @param {Object} data 注册信息
   * @returns {Promise}
   */
  register(data) {
    return axios.post('/auth/register', data)
  },
  
  /**
   * 发送手机验证码
   * @param {String} phone 手机号
   * @returns {Promise}
   */
  sendVerificationCode(phone) {
    return axios.post('/auth/send-code', { phone })
  },
  
  /**
   * 获取验证码图片
   * @returns {Promise}
   */
  getCaptcha() {
    return axios.get('/auth/captcha')
  },
  
  /**
   * 用户退出登录
   * @returns {Promise}
   */
  logout() {
    return axios.post('/auth/logout')
  }
}