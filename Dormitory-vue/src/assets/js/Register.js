import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'


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
        email: '',
        classroom:'',
        verificationCode: ''
    })

    // 表单验证规则
    const rules = reactive({
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
            // {
            //     validator: (rule, value, callback) => {
            //         if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,20}$/.test(value)) {
            //             callback(new Error('密码必须包含大小写字母和数字'))
            //         } else {
            //             callback()
            //         }
            //     },
            //     trigger: 'blur'
            // }
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
            { min: 5, max: 5, message: '学号必须为5  个字符', trigger: 'blur' }
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
        ],
        classroom:[
            { required: true, message: '请输入班级', trigger: 'blur' },
            { min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur' }
        ],
        verificationCode: [
            { required: true, message: '请输入验证码', trigger: 'blur' },
            { len: 6, message: '验证码为6位数字', trigger: 'blur' }
        ]
    })

    // 加载状态
    const loading = ref(false)
    const registerFormRef = ref(null)
    
    // 验证码相关状态
    const sendingCode = ref(false)
    const countdown = ref(0)
    const countdownTimer = ref(null)

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
                email: registerForm.email,    //
                classroom: registerForm.classroom,
                verificationCode: registerForm.verificationCode
            }

            console.log('提交的注册数据:', requestData) // 调试用

            // 调用注册API
            const response = await request.post('/stu/register', requestData);
            console.log('注册响应:', response) // 调试用

            if (response.code === "0") {
                ElMessage.success('注册成功！')
                router.push('/login')
            } else {
                ElMessage.error(response.msg || '注册失败')
            }
        } catch (error) {
            console.error('注册错误:', error)
            if (error.response) {
                ElMessage.error(error.response.data?.msg || '注册失败')
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

    // 发送验证码
    const sendVerificationCode = async () => {
        // 验证邮箱格式
        if (!registerForm.email) {
            ElMessage.error('请先输入邮箱')
            return
        }
        
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        if (!emailRegex.test(registerForm.email)) {
            ElMessage.error('请输入正确的邮箱格式')
            return
        }
        
        try {
            sendingCode.value = true
            
            console.log('发送验证码请求数据:', { email: registerForm.email })
            
            const response = await request.post('/stu/sendVerificationCode', {
                email: registerForm.email
            })
            
            console.log('验证码响应:', response)
            
            if (response.code === "0") {
                ElMessage.success('验证码已发送到您的邮箱')
                // 开始倒计时
                countdown.value = 60
                countdownTimer.value = setInterval(() => {
                    countdown.value--
                    if (countdown.value <= 0) {
                        clearInterval(countdownTimer.value)
                        countdownTimer.value = null
                    }
                }, 1000)
            } else {
                ElMessage.error(response.msg || '发送失败')
            }
        } catch (error) {
            console.error('发送验证码错误:', error)
            console.error('错误响应:', error.response)
            ElMessage.error(error.response?.data?.msg || error.message || '发送验证码失败')
        } finally {
            sendingCode.value = false
        }
    }
    
    // 清理定时器
    const clearCountdown = () => {
        if (countdownTimer.value) {
            clearInterval(countdownTimer.value)
            countdownTimer.value = null
        }
    }

    return {
        registerForm,
        rules,
        loading,
        registerFormRef,
        submitForm,
        sendingCode,
        countdown,
        sendVerificationCode,
        clearCountdown
    }
}