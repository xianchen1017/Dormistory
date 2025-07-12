import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "StuInfo",
    data() {
        // 手机号验证
        const checkPhone = (rule, value, callback) => {
            const phoneReg = /^1[3|4|5|6|7|8][0-9]{9}$/;
            if (!value) {
                return callback(new Error("电话号码不能为空"));
            }
            setTimeout(() => {
                if (!Number.isInteger(+value)) {
                    callback(new Error("请输入数字值"));
                } else {
                    if (phoneReg.test(value)) {
                        callback();
                    } else {
                        callback(new Error("电话号码格式不正确"));
                    }
                }
            }, 100);
        };
        const checkPass = (rule, value, callback) => {
            if (!this.editJudge) {
                if (value == "") {
                    callback(new Error("请再次输入密码"));
                } else if (value !== this.form.password) {
                    callback(new Error("两次输入密码不一致!"));
                } else {
                    callback();
                }
            } else {
                callback();
            }
        };
        return {
            showpassword: true,
            judgeAddOrEdit: true,
            loading: true,
            editJudge: true,
            disabled: false,
            judge: false,
            dialogVisible: false,
            assignRoomDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            availableRooms: [],
            availableBeds: [],
            form: {
                username: "",
                name: "",
                age: "",
                gender: "",
                phoneNum: "",
                email: "",
            },
            assignRoomForm: {
                username: "",
                name: "",
                dormBuildId: "",
                dormRoomId: "",
                bedId: ""
            },
            rules: {
                username: [
                    {required: true, message: "请输入学号", trigger: "blur"},
                    {
                        pattern: /^[a-zA-Z0-9]{4,9}$/,
                        message: "必须由 2 到 5 个字母或数字组成",
                        trigger: "blur",
                    },
                ],
                name: [
                    {required: true, message: "请输入姓名", trigger: "blur"},
                    {
                        pattern: /^(?:[\u4E00-\u9FA5·]{2,10})$/,
                        message: "必须由 2 到 10 个汉字组成",
                        trigger: "blur",
                    },
                ],
                age: [
                    {required: true, message: "请输入年龄", trigger: "blur"},
                    {type: "number", message: "年龄必须为数字值", trigger: "blur"},
                    {
                        pattern: /^(1|[1-9]\d?|100)$/,
                        message: "范围：1-100",
                        trigger: "blur",
                    },
                ],
                gender: [{required: true, message: "请选择性别", trigger: "change"}],
                phoneNum: [{required: true, validator: checkPhone, trigger: "blur"}],
                email: [
                    {type: "email", message: "请输入正确的邮箱地址", trigger: "blur"},
                ],
                password: [
                    {required: true, message: "请输入密码", trigger: "blur"},
                    {
                        min: 6,
                        max: 32,
                        message: "长度在 6 到 16 个字符",
                        trigger: "blur",
                    },
                ],
                checkPass: [{validator: checkPass, trigger: "blur"}],
            },
            assignRoomRules: {
                dormBuildId: [{required: true, message: "请选择楼栋", trigger: "change"}],
                dormRoomId: [{required: true, message: "请选择房间", trigger: "change"}],
                bedId: [{required: true, message: "请选择床位", trigger: "change"}],
            },
            editDisplay: {
                display: "block",
            },
            display: {
                display: "none",
            },
        };
    },
    created() {
        this.load();
        this.loading = true;
        setTimeout(() => {
            //设置延迟执行
            this.loading = false;
        }, 1000);
    },
    methods: {
        async load() {
            console.log("加载学生信息，参数:", {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            });
            
            request.get("/stu/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: this.search,
                },
            }).then((res) => {
                console.log("学生信息查询结果:", res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            }).catch((error) => {
                console.error("学生信息查询失败:", error);
                this.loading = false;
                ElMessage.error("查询失败");
            });
        },
        reset() {
            this.search = ''
            request.get("/stu/find", {
                params: {
                    pageNum: 1,
                    pageSize: this.pageSize,
                    search: this.search,
                },
            }).then((res) => {
                console.log(res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        filterTag(value, row) {
            return row.gender === value;
        },
        add() {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.judgeAddOrEdit = false;
                this.editDisplay = {display: "none"};
                this.disabled = false;
                this.form = {};
                this.judge = false;
            });
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.judge === false) {
                        //新增
                        request.post("/stu/add", this.form).then((res) => {
                            console.log(res);
                            if (res.code === "0") {
                                ElMessage({
                                    message: "新增成功",
                                    type: "success",
                                });
                                this.search = "";
                                this.loading = true;
                                this.load();
                                this.dialogVisible = false;
                            } else {
                                ElMessage({
                                    message: res.msg,
                                    type: "error",
                                });
                            }
                        });
                    } else {
                        //修改
                        request.put("/stu/update", this.form).then((res) => {
                            console.log(res);
                            if (res.code === "0") {
                                ElMessage({
                                    message: "修改成功",
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
                        });
                    }
                }
            });
        },
        cancel() {
            this.$refs.form.resetFields();
            this.display = {display: "none"};
            this.editJudge = true;
            this.disabled = true;
            this.showpassword = true;
            this.dialogVisible = false;
        },
        EditPass() {
            if (this.editJudge) {
                this.showpassword = false;
                this.display = {display: "flex"};
                this.disabled = false;
                this.editJudge = false;
            } else {
                this.showpassword = true;
                this.display = {display: "none"};
                this.editJudge = true;
                this.disabled = true;
            }
        },
        handleEdit(row) {
            //修改
            //判断操作类型
            this.judge = true;
            // 生拷贝
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
                this.judgeAddOrEdit = true;
                this.editDisplay = {display: "block"};
                this.disabled = true;
            });
        },
        async handleDelete(username) {
            //删除
            console.log(username);
            request.delete("/stu/delete/" + username).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "删除成功",
                        type: "success",
                    });
                    this.search = "";
                    this.load();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
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
        
        // 分配房间相关方法
        assignRoom(row) {
            this.assignRoomDialog = true;
            this.$nextTick(() => {
                this.$refs.assignRoomForm.resetFields();
                this.assignRoomForm.username = row.username;
                this.assignRoomForm.name = row.name;
                this.assignRoomForm.dormBuildId = "";
                this.assignRoomForm.dormRoomId = "";
                this.assignRoomForm.bedId = "";
                this.availableRooms = [];
                this.availableBeds = [];
            });
        },
        
        loadRooms() {
            if (!this.assignRoomForm.dormBuildId) return;
            
            // 加载指定楼栋的空房间
            request.get("/room/find", {
                params: {
                    pageNum: 1,
                    pageSize: 100,
                    search: "",
                },
            }).then((res) => {
                if (res.code === "0") {
                    // 过滤出指定楼栋且未满的房间
                    this.availableRooms = res.data.records.filter(room => 
                        room.dormBuildId === this.assignRoomForm.dormBuildId && 
                        room.currentCapacity < room.maxCapacity
                    );
                    console.log("可用房间:", this.availableRooms);
                }
            }).catch((error) => {
                console.error("加载房间失败:", error);
                ElMessage.error("加载房间失败");
            });
        },
        
        loadBeds() {
            if (!this.assignRoomForm.dormRoomId) return;
            
            // 找到选中的房间
            const selectedRoom = this.availableRooms.find(room => room.dormRoomId === this.assignRoomForm.dormRoomId);
            if (!selectedRoom) return;
            
            // 找出空床位
            this.availableBeds = [];
            if (!selectedRoom.firstBed) {
                this.availableBeds.push({id: 1, name: "1"});
            }
            if (!selectedRoom.secondBed) {
                this.availableBeds.push({id: 2, name: "2"});
            }
            if (!selectedRoom.thirdBed) {
                this.availableBeds.push({id: 3, name: "3"});
            }
            if (!selectedRoom.fourthBed) {
                this.availableBeds.push({id: 4, name: "4"});
            }
            
            console.log("可用床位:", this.availableBeds);
        },
        
        saveAssignRoom() {
            this.$refs.assignRoomForm.validate(async (valid) => {
                if (valid) {
                    // 检查学生是否已有房间
                    request.get("/room/judgeHadBed/" + this.assignRoomForm.username).then((res) => {
                        if (res.code === "0") {
                            // 学生没有房间，可以分配
                            this.doAssignRoom();
                        } else {
                            ElMessage.error("该学生已有房间，无法重复分配");
                        }
                    }).catch((error) => {
                        console.error("检查学生房间状态失败:", error);
                        ElMessage.error("检查学生房间状态失败");
                    });
                }
            });
        },
        
        doAssignRoom() {
            // 找到选中的房间
            const selectedRoom = this.availableRooms.find(room => room.dormRoomId === this.assignRoomForm.dormRoomId);
            if (!selectedRoom) {
                ElMessage.error("房间信息错误");
                return;
            }
            
            // 更新房间信息
            const bedName = this.getBedName(this.assignRoomForm.bedId);
            const updatedRoom = {
                ...selectedRoom,
                [bedName]: this.assignRoomForm.username,
                currentCapacity: selectedRoom.currentCapacity + 1
            };
            
            request.put("/room/update", updatedRoom).then((res) => {
                if (res.code === "0") {
                    ElMessage.success("房间分配成功");
                    this.assignRoomDialog = false;
                    this.load(); // 刷新学生列表
                } else {
                    ElMessage.error(res.msg || "分配失败");
                }
            }).catch((error) => {
                console.error("分配房间失败:", error);
                ElMessage.error("分配房间失败");
            });
        },
        
        getBedName(bedId) {
            switch (bedId) {
                case 1: return "firstBed";
                case 2: return "secondBed";
                case 3: return "thirdBed";
                case 4: return "fourthBed";
                default: return "";
            }
        },
        
        cancelAssignRoom() {
            this.assignRoomDialog = false;
            this.$refs.assignRoomForm.resetFields();
            this.availableRooms = [];
            this.availableBeds = [];
        },
        
        // 身份判断方法
        judgeIdentity() {
            const identity = sessionStorage.getItem('identity');
            if (identity === 'stu') {
                return 0;
            } else if (identity === 'dormManager') {
                return 1;
            } else if (identity === 'admin') {
                return 2;
            } else {
                return 0;
            }
        },
    },
};