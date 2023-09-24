const user={
    state: {
      username:sessionStorage.getItem("username"),//存储登陆的用户
    },
  getters: {
    getUser (state) {
      return state.username;
    }
  },
    mutations: {
       //修改用户状态
      saveUserName(state,username){
        state.username=username;
        sessionStorage.setItem("username",username);
      }
    },
    actions: {
      //从登陆界面获取用户信息

    }
}
export default user