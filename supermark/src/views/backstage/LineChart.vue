<template>

    <!-- echarts -->
    <div id="myChart" :style="{height:height,width:width}" />

</template>

<script>
import*as echarts from 'echarts';

import resize from './mixins/resize'
export default {
name: "LineChart",
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '1000px'
    },
    height: {
      type: String,
      default: '300px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required:true
    },
  },
  data(){
  return{
    chart:null,
  }
  },
  mounted() {

    this.$nextTick(()=>{
    this.drawLine();
  })
  },

  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      }
    },
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
     drawLine() {
      // 基于准备好的dom，初始化echarts实例
       this.chart = echarts.init(document.getElementById("myChart"))
       this.setOptions(this.chartData);
     },
    setOptions({orderCntData,orderCmtData,dayData} = {}) {
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: dayData,
          axisTick: {
            alignWithLabel: true
          },
          // axisLabel: {
          //   interval: 0,
          //   rotate: 40
          // }
        }],
        yAxis: [{
          type: 'value',
          name: '金额',
          axisLabel: {
            formatter: '{value} 元'
          },
          axisTick: {
            show: false
          },
        },
          {
          type: 'value',
          name: '订单数',
          axisLabel: {
            formatter: '{value} 笔'
          },
          axisTick: {
            show: false
          },
            minInterval: 1,
        }
          ],
        series: [{
          name: '订单总额',
          type: 'bar',
          stack: 'vistors',
          data: orderCmtData,
          animationDuration: 1500,
          animationEasing: 'cubicInOut'
        }, {
          name: '订单笔数',
          type: 'line',
          yAxisIndex: 1,
          data:orderCntData,
          itemStyle: {
            normal: {
              color: '#CC5A5A',
              lineStyle: {
                color: '#CC5A5A',
                width: 2
              }
            }
          },
          animationDuration: 2000,
          animationEasing: 'cubicInOut'
        }]
      })
    }
  },

}

</script>

<style scoped>

</style>