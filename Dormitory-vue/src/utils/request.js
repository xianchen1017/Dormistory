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
    const user = JSON.parse(localStorage.getItem('user'));
    // 排除登录路径 `/stu/login` 和 `/api/stu/login`
    if (
        config.url !== '/stu/login' &&
        config.url !== '/api/stu/login' &&
        user &&
        user.token
    ) {
        config.headers['Authorization'] = `Bearer ${user.token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 响应拦截器 - 处理401未授权
request.interceptors.response.use(
    response => {
        if (response.data.code === "401") {
            localStorage.clear()
            window.location.href = '/login'
        }
        return {
            code: response.data.code,
            msg: response.data.msg,
            data: response.data.data
        };
    },
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.clear()
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
);

export default request;