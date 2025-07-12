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
                pathRewrite: {
                    '^/api/main': '/main',      // 将/api/main重写为/main
                    '^/api/notice': '/notice',  // 将/api/notice重写为/notice
                    '^/api/repair': '/repair',  // 将/api/repair重写为/repair
                    '^/api/room': '/room',      // 将/api/room重写为/room
                    '^/api/building': '/building', // 将/api/building重写为/building
                    '^/api/admin': '/admin',    // 将/api/admin重写为/admin
                    '^/api/dormManager': '/dormManager', // 将/api/dormManager重写为/dormManager
                    '^/api/adjustRoom': '/adjustRoom', // 将/api/adjustRoom重写为/adjustRoom
                    '^/api/visitor': '/visitor', // 将/api/visitor重写为/visitor
                    '^/api/files': '/files'     // 将/api/files重写为/files
                    // 注意：不重写/api/stu，因为StudentController使用/api/stu路径
                }
            }
        }
    }
}

