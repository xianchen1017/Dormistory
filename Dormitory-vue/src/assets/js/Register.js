axios.defaults.baseURL = '/api'
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'


export function useRegister() {
    const router = useRouter()

    // 表单数据
    const registerForm = reactive({
        username: '',
        password: '',
        confirmPassword: '',
        studentId: '',
        phone: '',
        age: '',
        gender: '',
        email: ''
    })

    // 表单验证规则
    const rules = reactive({
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 4, max: 16, message: '长度在 4 到 16 个字符', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,20}$/.test(value)) {
                        callback(new Error('密码必须包含大小写字母和数字'))
                    } else {
                        callback()
                    }
                },
                trigger: 'blur'
            }
        ],
        confirmPassword: [
            { required: true, message: '请再次输入密码', trigger: 'blur' },
            {
                validator: (rule, value, callback) => {
                    if (value !== registerForm.password) {
                        callback(new Error('两次输入密码不一致!'))
                    } else {
                        callback()
                    }
                },
                trigger: 'blur'
            }
        ],
        studentId: [
            { required: true, message: '请输入学号', trigger: 'blur' },
            { pattern: /^\d{5}$/, message: '学号必须为5 位数字', trigger: 'blur' }
        ],
        phone: [
            { required: true, message: '请输入手机号', trigger: 'blur' },
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        age: [
            { required: true, message: '请输入年龄', trigger: 'blur' },
            { type: 'number', min: 15, max: 30, message: '年龄应在15-30岁之间', trigger: 'blur' }
        ],
        gender: [
            { required: true, message: '请选择性别', trigger: 'change' }
        ],
        email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ]
    })

    // 加载状态
    const loading = ref(false)
    const registerFormRef = ref(null)

    // 提交表单
    const submitForm = async () => {
        if (!registerFormRef.value) return
        try {
            await registerFormRef.value.validate()
            loading.value = true

            // 构造正确的请求数据对象
            const requestData = {
                username: registerForm.studentId, // 学号作为用户名
                password: registerForm.password,
                name: registerForm.username, // 用户名作为姓名
                phoneNum: registerForm.phone, // 注意与后端实体类字段名一致
                age: registerForm.age,       //
                gender: registerForm.gender, //
                email: registerForm.email    //
            }

            console.log('提交的注册数据:', requestData) // 调试用

            // 调用注册API
            const response = await axios.post('/stu/register', requestData, {
                withCredentials: false,  // 根据你的需求设置true或false
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log('注册响应:', response) // 调试用

            if (response.data.code === "0") {
                ElMessage.success('注册成功！')
                router.push('/login')
            } else {
                ElMessage.error(response.data.msg || '注册失败')
            }
        } catch (error) {
            if (error.response) {
                ElMessage.error(error.response.data.msg || '注册失败')
            } else if (error.message) {
                console.error('表单验证失败:', error)
                ElMessage.error('表单验证失败: ' + error.message)
            } else {
                ElMessage.error('注册失败，请稍后再试')
            }
        } finally {
            loading.value = false
        }
    }

    return {
        registerForm,
        rules,
        loading,
        registerFormRef,
        submitForm
    }
}