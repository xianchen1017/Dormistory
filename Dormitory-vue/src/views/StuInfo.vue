<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>学生信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <!--    功能区-->
        <div style="margin: 10px 0">
          <!--    搜索区-->
          <div style="margin: 10px 0">
            <el-input v-model="search" clearable placeholder="请输入姓名" prefix-icon="Search"
                      style="width: 20%"/>
            <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
            <el-button icon="refresh-left" style="margin-left: 10px" type="default" @click="reset"></el-button>
            <div style="float: right">
              <el-tooltip content="添加" placement="top">
                <el-button icon="plus" style="width: 50px" type="primary" @click="add"></el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <!--    表格-->
        <el-table v-loading="loading" :data="tableData" border max-height="705" style="width: 100%">
          <el-table-column label="#" type="index"/>
          <el-table-column label="学号" prop="username" sortable/>
          <el-table-column label="姓名" prop="name"/>
          <el-table-column label="班级" prop="classroom"/>

          <el-table-column
              :filter-method="filterTag"
              :filters="[
              { text: '男', value: '男' },
              { text: '女', value: '女' },
            ]"
              filter-placement="bottom-end"
              label="性别"
              prop="gender"
          />
          <el-table-column label="年龄" prop="age" sortable/>
          <el-table-column label="手机号" prop="phoneNum"/>
          <el-table-column :show-overflow-tooltip="true" label="邮箱" prop="email"/>
          <!--      操作栏-->
          <el-table-column label="操作" width="200px">
            <template #default="scope">
              <el-button icon="Edit" type="primary" @click="handleEdit(scope.row)"></el-button>
              <el-button v-if="judgeIdentity()===1" icon="House" type="success" @click="assignRoom(scope.row)">分配房间</el-button>
              <el-popconfirm title="确认删除？" @confirm="handleDelete(scope.row.username)">
                <template #reference>
                  <el-button icon="Delete" type="danger"></el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <!--分页-->
        <div style="margin: 10px 0">
          <el-pagination
              v-model:currentPage="currentPage"
              :page-size="pageSize"
              :page-sizes="[10, 20]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          >
          </el-pagination>
        </div>
        <div>
          <!--      弹窗-->
          <el-dialog v-model="dialogVisible" title="操作" width="30%" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
              <el-form-item label="学号" prop="username">
                <el-input v-model="form.username" :disabled="judgeAddOrEdit" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" :disabled="disabled" :show-password="showpassword"
                          style="width: 80%"></el-input>
                <el-tooltip content="修改密码" placement="right">
                  <el-icon :style="editDisplay" size="large" style="margin-left: 5px; cursor: pointer"
                           @click="EditPass">
                    <edit/>
                  </el-icon>
                </el-tooltip>
              </el-form-item>
              <el-form-item :style="display" label="确认密码" prop="checkPass">
                <el-input v-model="form.checkPass" :show-password="showpassword" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="年龄" prop="age">
                <el-input v-model.number="form.age" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio v-model="form.gender" label="男">男</el-radio>
                <el-radio v-model="form.gender" label="女">女</el-radio>
              </el-form-item>
              <el-form-item label="手机号" prop="phoneNum">
                <el-input v-model.number="form.phoneNum" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="邮箱地址" prop="email">
                <el-input v-model="form.email" style="width: 80%"></el-input>
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
              </span>
            </template>
          </el-dialog>
          
          <!-- 分配房间弹窗 -->
          <el-dialog v-model="assignRoomDialog" title="分配房间" width="40%" @close="cancelAssignRoom">
            <el-form ref="assignRoomForm" :model="assignRoomForm" :rules="assignRoomRules" label-width="120px">
              <el-form-item label="学生学号" prop="username">
                <el-input v-model="assignRoomForm.username" disabled style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="学生姓名" prop="name">
                <el-input v-model="assignRoomForm.name" disabled style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="楼栋号" prop="dormBuildId">
                <el-select v-model="assignRoomForm.dormBuildId" placeholder="请选择楼栋" style="width: 80%" @change="loadRooms">
                  <el-option label="1号楼" :value="1"></el-option>
                  <el-option label="2号楼" :value="2"></el-option>
                  <el-option label="3号楼" :value="3"></el-option>
                  <el-option label="4号楼" :value="4"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="房间号" prop="dormRoomId">
                <el-select v-model="assignRoomForm.dormRoomId" placeholder="请选择房间" style="width: 80%" @change="loadBeds">
                  <el-option 
                    v-for="room in availableRooms" 
                    :key="room.dormRoomId" 
                    :label="`${room.dormRoomId} (${room.currentCapacity}/${room.maxCapacity})`" 
                    :value="room.dormRoomId">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="床位号" prop="bedId">
                <el-select v-model="assignRoomForm.bedId" placeholder="请选择床位" style="width: 80%">
                  <el-option 
                    v-for="bed in availableBeds" 
                    :key="bed.id" 
                    :label="`${bed.name}号床`" 
                    :value="bed.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="cancelAssignRoom">取 消</el-button>
                <el-button type="primary" @click="saveAssignRoom">确 定</el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/StuInfo.js"></script>