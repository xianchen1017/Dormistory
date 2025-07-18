<template>
  <div>
      <h2>入住报表</h2>
      <div style="display: flex; flex-wrap: wrap; gap: 32px;">
        <div>
          <h3>本月各房型入住人数</h3>
          <div ref="pieChart" style="width: 400px; height: 300px;"></div>
        </div>
        <div>
          <h3>本月与去年同期入住人数对比</h3>
          <div ref="barChart" style="width: 400px; height: 300px;"></div>
        </div>
        <div>
          <h3>本季度每月入住人数</h3>
          <div ref="quarterChart" style="width: 400px; height: 300px;"></div>
        </div>
        <div>
          <h3>全年每月入住人数</h3>
          <div ref="yearChart" style="width: 400px; height: 300px;"></div>
        </div>
      </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from "@/utils/request"

export default {
  name: "Report",
  data() {
    return {
      pieData: [],
      barData: {},
      quarterData: [],
      yearData: [],
      debug: {
        monthly: '',
        monthlyCompare: '',
        quarterly: '',
        yearly: '',
        error: ''
      },
      pieChartInstance: null,
    }
  },
  computed: {
    isAdmin() {
      const user = JSON.parse(sessionStorage.getItem("user") || '{}');
      return user.role === 'admin';
    }
  },
  mounted() {
    this.loadMonthly();
    this.loadMonthlyCompare();
    this.loadQuarterly();
    this.loadYearly();
  },
  methods: {
    loadMonthly() {
      request.get("/report/monthly").then(res => {
        console.log('【前端收到的原始数据】', res);
        this.debug.monthly = JSON.stringify(res, null, 2);
        this.pieData = res.data;
        this.$nextTick(() => {
          this.drawPie();
        });
      }).catch(err => {
        this.debug.error = 'monthly: ' + err;
        console.log('loadMonthly error', err);
      });
    },
    loadMonthlyCompare() {
      request.get("/report/monthly-compare").then(res => {
        this.debug.monthlyCompare = JSON.stringify(res.data, null, 2);
        this.barData = res.data;
        this.drawBar();
      }).catch(err => {
        this.debug.error = 'monthly-compare: ' + err;
      });
    },
    loadQuarterly() {
      request.get("/report/quarterly").then(res => {
        this.debug.quarterly = JSON.stringify(res, null, 2);
        this.quarterData = Array.isArray(res.data) ? res.data : [];
        this.drawQuarter();
      }).catch(err => {
        this.debug.error = 'quarterly: ' + err;
        console.log('loadQuarterly error', err);
      });
    },
    loadYearly() {
      request.get("/report/yearly").then(res => {
        this.debug.yearly = JSON.stringify(res, null, 2);
        this.yearData = Array.isArray(res.data) ? res.data : [];
        this.drawYear();
      }).catch(err => {
        this.debug.error = 'yearly: ' + err;
      });
    },
    drawPie() {
      try {
        console.log('【传给ECharts的pieData】', this.pieData);
        if (this.pieChartInstance) {
          this.pieChartInstance.dispose();
        }
        this.pieChartInstance = echarts.init(this.$refs.pieChart);
        this.pieChartInstance.setOption({
          title: { text: '本月各房型入住人数', left: 'center' },
          tooltip: { trigger: 'item' },
          legend: { bottom: 10, left: 'center' },
          series: [{
            name: '人数',
            type: 'pie',
            radius: '50%',
            data: this.pieData,
            emphasis: {
              itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' }
            }
          }]
        });
      } catch (e) {
        this.debug.error = 'drawPie: ' + e;
      }
    },
    drawBar() {
      try {
        let chart = echarts.init(this.$refs.barChart);
        chart.setOption({
          title: { text: '本月与去年同期入住人数对比', left: 'center' },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: ['本月', '去年同期'] },
          yAxis: { type: 'value' },
          series: [{
            data: [this.barData.thisMonth, this.barData.lastYearMonth],
            type: 'bar'
          }]
        });
      } catch (e) {
        this.debug.error = 'drawBar: ' + e;
      }
    },
    drawQuarter() {
      try {
        const quarterMonths = [7, 8, 9]; // 假设当前季度
        const dataMap = {};
        this.quarterData.forEach(i => { dataMap[i.month] = i.total; });
        const seriesData = quarterMonths.map(m => dataMap[m] || 0);
        let chart = echarts.init(this.$refs.quarterChart);
        chart.setOption({
          title: { text: '本季度每月入住人数', left: 'center' },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: quarterMonths },
          yAxis: { type: 'value' },
          series: [{
            data: seriesData,
            type: 'line'
          }]
        });
      } catch (e) {
        this.debug.error = 'drawQuarter: ' + e;
        console.log('drawQuarter error', e);
      }
    },
    drawYear() {
      try {
        const rawYearData = JSON.parse(JSON.stringify(this.yearData));
        const months = Array.from({length: 12}, (_, i) => i + 1);
        const dataMap = {};
        // 关键：month转成数字，保证key类型一致
        rawYearData.forEach(i => { dataMap[Number(i.month)] = i.total; });
        const seriesData = months.map(m => dataMap[m] || 0);
        let chart = echarts.init(this.$refs.yearChart);
        chart.setOption({
          title: { text: '全年每月入住人数', left: 'center' },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: months },
          yAxis: { type: 'value', max: 12 },
          series: [{
            data: seriesData,
            type: 'bar'
          }]
        });
      } catch (e) {
        this.debug.error = 'drawYear: ' + e;
        console.log('drawYear error', e);
      }
    }
  }
}
</script>