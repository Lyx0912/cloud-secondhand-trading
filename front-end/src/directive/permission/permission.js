import store from '@/store'

function checkPermission(el, binding) {
  const { value } = binding
  const permissions = store.getters && store.getters.permissions
  if (value) {
    if (value) {
      if (permissions.indexOf(value) === -1) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  } else {
    throw new Error(`need roles! Like v-permission="['admin','editor']"`)
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
