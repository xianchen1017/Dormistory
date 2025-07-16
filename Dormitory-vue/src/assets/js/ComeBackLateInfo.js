import request from "@/utils/request";
const { ElMessage } = require("element-plus");

export default {
    name: "ComeBackLateInfo",
    data() {
        return {
            loading: true,
            dialogVisible: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            form: {
                id: "",
                studentName: "",
                dormRoom: "",
                lateTime: "",
                reason: "",
                remark: ""
            },
            rules: {
                studentName: [
                    { required: true, message: "请输入学生姓名", trigger: "blur" }
                ],
                dormRoom: [
                    { required: true, message: "请输入宿舍号", trigger: "blur" },
                ],
                lateTime: [
                    { required: true, message: "请选择晚归时间", trigger: "change" }
                ],
                reason: [
                    { required: true, message: "请输入晚归原因", trigger: "blur" }
                ]
            }
        };
    },
    created() {
        this.load();
    },
    methods: {
        async load() {
            this.loading = true;
            try {
                // 关键修改：去掉 URL 中的 /api 前缀（因为 baseURL 已包含）
                const res = await request.get("/come-back-late/find", {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize,
                        search: this.search
                    }
                });

                // 调试日志
                console.log("请求成功:", {
                    params: this.$data,
                    response: res.data
                });

                this.tableData = res.data.records;
                this.total = res.data.total;
            } catch (error) {
                console.error("请求失败:", {
                    error: error.response?.data || error.message,
                    requestConfig: error.config
                });
                ElMessage.error("数据加载失败");
            } finally {
                this.loading = false;
            }
        },

        reset() {
            this.search = "";
            this.currentPage = 1;
            this.load();
        },

        add() {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = {
                    lateTime: new Date().toISOString().substr(0, 16)
                };
            });
        },

        async save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    try {
                        const url = this.form.id ? "/come-back-late/update" : "/come-back-late/add";
                        const method = this.form.id ? "put" : "post";

                        const res = await request[method](url, this.form);
                        if (res.code === "0") {
                            ElMessage.success(this.form.id ? "修改成功" : "新增成功");
                            this.dialogVisible = false;
                            this.load();
                        } else {
                            ElMessage.error(res.msg);
                        }
                    } catch (error) {
                        console.error("操作失败:", error);
                        ElMessage.error("操作失败");
                    }
                }
            });
        },

        cancel() {
            this.dialogVisible = false;
            this.$refs.form.resetFields();
        },

        handleEdit(row) {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.form = { ...row };
            });
        },

        async handleDelete(id) {
            try {
                const res = await request.delete(`/come-back-late/delete/${id}`);
                if (res.code === "0") {
                    ElMessage.success("删除成功");
                    this.load();
                } else {
                    ElMessage.error(res.msg);
                }
            } catch (error) {
                console.error("删除失败:", error);
                ElMessage.error("删除失败");
            }
        },

        handleSizeChange(pageSize) {
            this.pageSize = pageSize;
            this.load();
        },

        handleCurrentChange(pageNum) {
            this.currentPage = pageNum;
            this.load();
        },

        formatTime(time) {
            return time ? new Date(time).toLocaleString() : "";
        }
    }
};