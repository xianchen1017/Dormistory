import axios from 'axios'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
request.interceptors.request.use(config => {
    const user = sessionStorage.getItem('user');
    // 排除登录路径
    if (
        config.url !== '/api/stu/login' &&
        config.url !== '/api/dormManager/login' &&
        config.url !== '/api/admin/login' &&
        user
    ) {
        // 使用session认证，不需要额外的Authorization头
        // Spring Security会自动处理session认证
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 响应拦截器 - 处理401未授权
request.interceptors.response.use(
    response => {
        console.log('响应拦截器 - 原始响应:', response)
        
        if (response.data.code === "401") {
            sessionStorage.clear()
            window.location.href = '/login'
        }
        
        // 直接返回响应数据，让调用方处理
        return response.data;
    },
    error => {
        console.error('响应拦截器 - 错误:', error)
        if (error.response && error.response.status === 401) {
            sessionStorage.clear()
            window.location.href = '/login'
        }
        // 如果是网络错误或其他错误，返回一个标准格式的错误对象
        return Promise.reject({
            response: {
                data: {
                    code: "-1",
                    msg: error.message || "网络错误",
                    data: null
                }
            }
        })
    }
);

export default request;