import request from "@/utils/request";
import { ElMessage } from "element-plus";

export default {
    name: "ApplyChangeRoom",
  data() {
        const checkRoomState = (rule, value, callback) => {
            if (!value) {
                callback(new Error("请输入目标房间号"));
                return;
            }
            this.dormRoomId = value;
            request.get(`/room/checkRoomState/${value}`).then((res) => {
                if (res.code === "0") {
                    callback();
                } else {
                    callback(new Error(res.msg));
                }
            });
        };
        const checkBedState = (rule, value, callback) => {
            if (!this.dormRoomId) {
                callback(new Error("请先选择目标房间"));
                return;
            }
            request.get(`/room/checkBedState/${this.dormRoomId}/${value}`).then((res) => {
                if (res.code === "0") {
                    callback();
                } else {
                    callback(new Error(res.msg));
                }
            });
        };
        return {
            loading: true,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            form: {},
            dormRoomId: 0,
            judgeOption: false,
            username: '',
            name: '',
            rules: {
                towardsRoomId: [{ validator: checkRoomState, trigger: "blur" }],
                towardsBedId: [{ validator: checkBedState, trigger: "blur" }],
            },
        };
  },
  mounted() {
        this.getUserInfo();
        this.load();
  },
  methods: {
        // 获取用户信息
        getUserInfo() {
            const user = JSON.parse(sessionStorage.getItem("user") || '{}');
            this.username = user.username || '';
            this.name = user.name || '';
        },
        
        // 加载数据
        async load() {
            try {
                // 学生只能查看自己的申请，所以搜索条件设置为自己的学号
                const searchParam = this.search || this.username;
                
                console.log("=== 前端调宿申请查询调试信息 ===");
                console.log("发送的请求参数:");
                console.log("  pageNum:", this.currentPage);
                console.log("  pageSize:", this.pageSize);
                console.log("  search:", searchParam);
                console.log("  当前用户:", this.username);
                
                const res = await request.get("/adjustRoom/find", {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize,
                        search: searchParam,
                    },
                });
                
                console.log("接收到的响应数据:");
                console.log("  完整响应:", res);
                console.log("  响应code:", res.code);
                console.log("  响应msg:", res.msg);
                console.log("  响应data:", res.data);
                if (res.data) {
                    console.log("    总记录数:", res.data.total);
                    console.log("    当前页记录数:", res.data.records ? res.data.records.length : 0);
                    console.log("    记录内容:", res.data.records);
                }
                console.log("=== 前端调试信息结束 ===");
                
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            } catch (error) {
                console.error("加载调宿申请失败:", error);
                ElMessage.error("加载数据失败");
                this.loading = false;
            }
        },
        
        // 重置搜索
        reset() {
            this.search = '';
            this.currentPage = 1;
            // 重置后仍然显示当前学生的申请
            this.load();
        },
        
        // 添加申请
        add() {
            this.judgeOption = true;
            this.form = {
                username: this.username,
                name: this.name,
                currentRoomId: '',
                currentBedId: '',
                towardsRoomId: '',
                towardsBedId: '',
                state: '未处理',
                applyTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
            };
            this.getCurrentRoomInfo();
            this.dialogVisible = true;
        },
        
        // 获取当前房间信息
        getCurrentRoomInfo() {
            if (!this.username) {
                ElMessage.error("用户信息获取失败");
                return;
            }
            request.get(`/room/getMyRoom/${this.username}`).then((res) => {
                if (res.code === "0" && res.data) {
                    this.form.currentRoomId = res.data.dormRoomId;
                    // 根据床位信息确定当前床位号
                    if (res.data.firstBed === this.username) {
                        this.form.currentBedId = 1;
                    } else if (res.data.secondBed === this.username) {
                        this.form.currentBedId = 2;
                    } else if (res.data.thirdBed === this.username) {
                        this.form.currentBedId = 3;
                    } else if (res.data.fourthBed === this.username) {
                        this.form.currentBedId = 4;
                    }
                } else {
                    ElMessage.error("获取房间信息失败");
                }
            });
        },
        
        // 编辑申请
        handleEdit(row) {
            this.judgeOption = false;
            this.form = { ...row };
            this.dialogVisible = true;
        },
        
        // 显示详情
        showDetail(row) {
            this.form = { ...row };
            this.detailDialog = true;
        },
        
        // 保存申请
        save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    try {
                        if (this.judgeOption) {
                            // 新增申请
                            console.log("=== 前端调宿申请添加调试信息 ===");
                            console.log("发送的表单数据:");
                            console.log("  完整表单:", this.form);
                            console.log("  学号:", this.form.username);
                            console.log("  姓名:", this.form.name);
                            console.log("  当前房间:", this.form.currentRoomId);
                            console.log("  当前床位:", this.form.currentBedId);
                            console.log("  目标房间:", this.form.towardsRoomId);
                            console.log("  目标床位:", this.form.towardsBedId);
                            console.log("  状态:", this.form.state);
                            console.log("  申请时间:", this.form.applyTime);
                            
                            const res = await request.post("/adjustRoom/add", this.form);
                            
                            console.log("接收到的响应:");
                            console.log("  完整响应:", res);
                            console.log("  响应code:", res.code);
                            console.log("  响应msg:", res.msg);
                            console.log("=== 前端添加调试信息结束 ===");
                            
                            if (res.code === "0") {
                                ElMessage.success("申请提交成功");
                                this.dialogVisible = false;
                                this.load();
                            } else {
                                ElMessage.error(res.msg || "申请提交失败");
                            }
                        } else {
                            // 更新申请
                            const res = await request.put("/adjustRoom/update/" + false, this.form);
                            if (res.code === "0") {
                                ElMessage.success("申请更新成功");
                                this.dialogVisible = false;
                                this.load();
                            } else {
                                ElMessage.error(res.msg || "申请更新失败");
                            }
                        }
                    } catch (error) {
                        console.error("保存申请失败:", error);
                        ElMessage.error("操作失败");
                    }
                }
            });
        },
        
        // 取消操作
        cancel() {
            this.dialogVisible = false;
            this.detailDialog = false;
            this.form = {};
        },
        
        // 分页大小改变
        handleSizeChange(pageSize) {
            this.pageSize = pageSize;
            this.load();
        },
        
        // 当前页改变
        handleCurrentChange(currentPage) {
            this.currentPage = currentPage;
            this.load();
        },
        
        // 状态过滤
        filterTag(value, row) {
            return row.state === value;
        }
    }
};