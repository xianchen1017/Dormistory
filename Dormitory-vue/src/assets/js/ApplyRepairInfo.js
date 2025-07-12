import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "ApplyRepairInfo",
    components: {},
    data() {
        return {
            loading: true,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            detail: {},
            name: '',
            username: '',
            form: {},
            room: {
                dormRoomId: '',
                dormBuildId: '',
            },
            rules: {
                title: [{required: true, message: "请输入标题", trigger: "blur"}],
                content: [{required: true, message: "请输入内容", trigger: "blur"}],
                orderBuildTime: [{required: true, message: "请选择时间", trigger: "blur"},],
            },
        };
    },
    created() {
        this.init()
        this.getInfo()
        this.load()
        this.loading = true
        setTimeout(() => {
            //设置延迟执行
            this.loading = false
        }, 1000);
    },
    methods: {
        init() {
            this.form = JSON.parse(sessionStorage.getItem("user"));
            this.name = this.form.name;
            this.username = this.form.username;
        },
        async load() {
            // 查询所有报修记录
            console.log("查询所有报修申请");
            console.log("查询参数:", {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            });
            
            request.get("/repair/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: this.search,
                },
            }).then((res) => {
                console.log("报修申请查询结果:", res);
                console.log("返回的数据记录数:", res.data.records ? res.data.records.length : 0);
                console.log("总记录数:", res.data.total);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            }).catch((error) => {
                console.error("报修申请查询失败:", error);
                this.loading = false;
            });
        },
        getInfo() {
            request.get("/room/getMyRoom/" + this.username).then((res) => {
                if (res.code === "0") {
                    this.room = res.data;
                    console.log(this.room);
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        filterTag(value, row) {
            return row.state === value;
        },
        showDetail(row) {
            this.detailDialog = true;
            this.$nextTick(() => {
                this.detail = row;
            });
        },
        closeDetails() {
            this.detailDialog = false;
        },
        add() {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form.repairer = this.username // 使用学号而不是姓名
                this.form.dormBuildId = this.room.dormBuildId
                this.form.dormRoomId = this.room.dormRoomId
            });
        },
        save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    //新增
                    // 确保数据类型正确
                    const formData = {
                        ...this.form,
                        dormBuildId: parseInt(this.form.dormBuildId),
                        dormRoomId: parseInt(this.form.dormRoomId),
                        orderBuildTime: new Date().toISOString().slice(0, 19).replace('T', ' '), // 添加报修时间
                        state: '待处理' // 设置初始状态
                    };
                    
                    console.log("提交的报修申请数据:", formData);
                    await request.post("/repair/add", formData).then((res) => {
                        if (res.code === "0") {
                            ElMessage({
                                message: "新增成功",
                                type: "success",
                            });
                            this.search = "";
                            this.load();
                            this.dialogVisible = false;
                        } else {
                            ElMessage({
                                message: res.msg,
                                type: "error",
                            });
                        }
                    }).catch((error) => {
                        console.error("报修申请添加失败:", error);
                        ElMessage({
                            message: "添加失败，请检查数据格式",
                            type: "error",
                        });
                    });
                }
            })
        },
        cancel() {
            this.$refs.form.resetFields();
            this.dialogVisible = false;
        },
        handleSizeChange(pageSize) {
            //改变每页个数
            this.pageSize = pageSize;
            this.load();
        },
        handleCurrentChange(pageNum) {
            //改变页码
            this.currentPage = pageNum;
            this.load();
        },
    },
};