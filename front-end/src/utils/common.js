const indexOf = (str, cha, num) => {
  var x = str.indexOf(cha)

  for (var i = 0; i < num; i++) {
    x = str.indexOf(cha, x + 1)
  }
  return x
}
const clear = (form) => {
  if (form) {
    Object.keys(form).forEach(key => {
      if (form[key] instanceof Array) {
        form[key] = []
      } else {
        form[key] = ''
      }
    })
  }
}
export default {
  indexOf, clear
}
