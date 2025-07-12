// 跨域配置
module.exports = {

    publicPath: './',// vue-cli3.3+新版本使用

    devServer: {
        //记住，别写错了devServer//设置本地默认端口  选填
        hot: false,
        liveReload: false,
        // port: 9876,
        proxy: {                 //设置代理，必须填
            '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
                target: 'http://localhost:9090',     //代理的目标地址
                changeOrigin: true,              //是否设置同源，输入是的
                // 不需要pathRewrite，直接转发/api到后端
            }
        }
    }
}

