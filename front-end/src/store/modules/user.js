import { login, logout, getInfo } from '@/api/admin/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { constantRoutes, resetRouter } from '@/router'
import { menuRoutes } from '@/api/admin/menu'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    routes: [],
    permissions: [],
    nickname: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ROUTES: (state, routes) => {
    state.routes = constantRoutes.concat(routes)
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  },
  SET_NICKNAME: (state, nickname) => {
    state.nickname = nickname
  }
}

const actions = {
  serPermissions({ commit }, permissions) {
    commit('SET_PERMISSIONS', permissions)
  },
  // user login
  login({ commit }, userInfo) {
    const { username, password, grant_type,captcha } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, grant_type: grant_type,captcha: captcha }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.access_token)
        setToken(data.access_token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const { username, avatar,nickname } = data

        commit('SET_NAME', username)
        commit('SET_AVATAR', avatar)
        commit('SET_NICKNAME', nickname)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      removeToken() // must remove  token  first
      resetRouter()
      commit('SET_TOKEN','')
      resolve()
    })
  },

  // user menu
  listMenuTree({ commit, state }) {
    return new Promise((resolve, reject) => {
      menuRoutes().then((res) => {
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setRoutes({ commit }, routes) {
    commit('SET_ROUTES', routes)
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

