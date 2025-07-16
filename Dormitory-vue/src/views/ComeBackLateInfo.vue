<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍管理</el-breadcrumb-item>
      <el-breadcrumb-item>晚归记录</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <!-- 搜索区 -->
      <div style="margin: 10px 0">
        <el-input
            v-model="search"
            clearable
            placeholder="搜索姓名/宿舍号"
            prefix-icon="Search"
            style="width: 30%"
            @keyup.enter="load"
        />
        <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
        <el-button icon="refresh-left" type="default" @click="reset">重置</el-button>

        <div style="float: right">
          <el-button icon="plus" type="primary" @click="add">新增记录</el-button>
        </div>
      </div>

      <!-- 表格 -->
      <el-table
          v-loading="loading"
          :data="tableData"
          border
          style="width: 100%"
      >
        <el-table-column label="#" type="index" width="50" />
        <el-table-column label="姓名" prop="studentName" sortable width="120" />
        <el-table-column label="宿舍号" prop="dormRoom" width="120" />
        <el-table-column label="晚归时间" width="180">
          <template #default="{row}">
            {{ formatTime(row.lateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="晚归原因" prop="reason" />
        <el-table-column label="备注" prop="remark" />

        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button icon="Edit" type="primary" @click="handleEdit(scope.row)"></el-button>
            <el-popconfirm title="确认删除这条记录？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button icon="Delete" type="danger"></el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin: 20px 0; text-align: center">
        <el-pagination
            v-model:currentPage="currentPage"
            :page-size="pageSize"
            :page-sizes="[5, 10, 20]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            background
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>

      <!-- 新增/编辑弹窗 -->
      <el-dialog
          v-model="dialogVisible"
          :title="form.id ? '编辑晚归记录' : '新增晚归记录'"
          width="500px"
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="90px">
          <el-form-item label="学生姓名" prop="studentName">
            <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
          </el-form-item>

          <el-form-item label="宿舍号" prop="dormRoom">
            <el-input v-model="form.dormRoom" placeholder="如：8栋101" />
          </el-form-item>

          <el-form-item label="晚归时间" prop="lateTime">
            <el-date-picker
                v-model="form.lateTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="选择晚归时间"
                style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="晚归原因" prop="reason">
            <el-input
                v-model="form.reason"
                type="textarea"
                :rows="3"
                placeholder="请输入详细原因"
                show-word-limit
                maxlength="200"
            />
          </el-form-item>

          <el-form-item label="备注">
            <el-input
                v-model="form.remark"
                type="textarea"
                :rows="2"
                placeholder="额外说明信息"
            />
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="cancel">取消</el-button>
            <el-button type="primary" @click="save">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script src="@/assets/js/ComeBackLateInfo.js"></script>

<style scoped>

</style>