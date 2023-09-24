import request from '@/utils/request'

export function logins(params){
    return request({
        url: '/cloud-member/loginLog/login',
        method: 'post',
        params
    })
}