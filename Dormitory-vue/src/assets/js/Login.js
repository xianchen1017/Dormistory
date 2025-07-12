import request from "@/utils/request";
import { ElMessage } from 'element-plus';

export default {
    name: "Login",
    data() {
        return {
            form: {
                username: "",
                password: "",
                identity: "stu" // 设置默认值
            },
            rules: {
                username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
                password: [{ required: true, message: "请输入密码", trigger: "blur" }]
            }
        };
    },
    computed: {
        disabled() {
            return this.form.username && this.form.password && this.form.identity;
        }
    },
    methods: {
        async login() {
            try {
                const res = await request.post(
                    `/${this.form.identity}/login`,
                    {
                        username: this.form.username,
                        password: this.form.password
                    }
                );
                console.log("登录响应:", res);

                if (res.code === "0") {
                    sessionStorage.setItem("user", JSON.stringify(res.data));
                    sessionStorage.setItem("identity", this.form.identity);

                    // 直接跳转到home页面，不进行路由验证
                    this.$router.push("/home").catch(err => {
                        console.error("路由跳转错误:", err);
                        ElMessage.error("跳转页面失败");
                    });
                } else {
                    ElMessage.error(res.msg || "登录失败");
                }
            } catch (error) {
                console.error("登录错误:", error);
                if (error.response) {
                    ElMessage.error(`HTTP ${error.response.status}: ${error.response.data.msg || error.message}`);
                } else if (error.request) {
                    ElMessage.error("请求失败，请检查网络");
                } else {
                    ElMessage.error(`登录异常: ${error.message}`);
                }
            }
        }
    }
};