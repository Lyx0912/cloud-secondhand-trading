<template>
  <div class="dashboard-container">
    <el-row :gutter="25" style="margin-bottom: 0px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '10px'}" style="border-radius: 10px">
          <el-image style="float: left;margin-left: 15px" :src="require('@/assets/background/user.svg')"></el-image>
          <div class="info">
            <h5 class="info-desc">用户数量</h5>
            <span class="info-desc-num">2000</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '10px'}" style="border-radius: 10px" >
          <el-image style="float: left;margin-left: 15px" :src="require('@/assets/background/order.svg')"></el-image>
          <div class="info">
            <h5 class="info-desc">订单数量</h5>
            <span class="info-desc-num">3501</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '10px'}" style="border-radius: 10px">
          <el-image style="float: left;margin-left: 15px" :src="require('@/assets/background/money.svg')"></el-image>
          <div class="info">
            <h5 class="info-desc">交易金额</h5>
            <span class="info-desc-num">3508988.5</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '10px'}" style="border-radius: 10px">
          <el-image style="float: left;margin-left: 15px" :src="require('@/assets/background/goods.svg')"></el-image>
          <div class="info">
            <h5 class="info-desc">物品数量</h5>
            <span class="info-desc-num">3508</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div  ref="charLine" id="char-line" class="char-line"></div>
    <div id="char-ano" class="char-ano">
      <div id="char-bar"  class="char-bar"></div>
      <div id="char-pie" class="char-pie"></div>
    </div>
  </div>

</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name',
      'nickname'
    ])
  },
  mounted() {
    this.drawChar()
  },
  methods: {
    drawChar() {
      var chartLine = this.$echarts.init(document.getElementById('char-line'))
      var charBar = this.$echarts.init(document.getElementById('char-bar'))
      var charPie = this.$echarts.init(document.getElementById('char-pie'))

      var optionLine = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['订单数', '交易额']
        },
        grid: {
          left: '3%',
          right: '3%',
          bottom: 25,
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          axisLabel: {
            textStyle: {
              color: '#4AACDB',//坐标值得具体的颜色
            }
          },
          axisLine: {
            show: false,
            minInterval:1,
            lineStyle: {
              type: 'solid',
              color: '#CCCCCC',//左边线的颜色
              width: '1'//坐标线的宽度
            }
          },
          lineStyle: {
            type: 'solid',
            color: '#CCCCCC',//左边线的颜色
            width: '1'//坐标线的宽度
          },
          type: 'category',
          boundaryGap: false,
          data: ['2015-1', '2015-2', '2015-3', '2015-4', '2015-5', '2015-6', '2015-7', '2015-8', '2015-9', '2015-10', '2015-11', '2015-12']
        },
        yAxis: [
          {
            type: 'value',
            axisLine: {
              show: false,
              minInterval:1,
              lineStyle: {
                type: 'solid',
                color: '#CCCCCC',//左边线的颜色
                width: '1'//坐标线的宽度
              }
            },
            axisLabel: {
              textStyle: {
                color: '#4AACDB',//坐标值得具体的颜色
              }
            },
            axisTick:false,
            splitLine: {  //坐标轴在grid区域中的分隔线（网格中的横线）
              show: true,
              lineStyle: {
                color: ['#EEEEEE'],
                width: 1,
              }
            },
            splitArea: {//坐标轴在grid区域中的分隔区域（间隔显示网格区域）
              interval: 1, //显示间隔的规律
              show: true,
              areaStyle: {//分隔区域的样式
                color: ['rgba(255,255,255)', 'rgba(249,249,249)']
              }
            }
          }
        ],
        series: [
          {
            name: '订单数',
            type: 'line',
            smooth: true,
            areaStyle: {
              normal: {
                color: '#F6F9FE'
              }
            },
            itemStyle: {
              normal: {
                color: '#3888FA'
              }
            },
            data: [
              2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3
            ],
            animationDuration: 2500,
            animationEasing: 'cubicInOut'
          },
          {
            name: '交易额',
            type: 'line',
            smooth: true,
            areaStyle: {
              normal: {
                color: '#FFF9FE'
              }
            },
            itemStyle: {
              normal: {
                color: '#FF005A'
              }
            },
            data: [
              3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3, 0.7
            ],
            animationDuration: 2500,
            animationEasing: 'quadraticOut'
          }
        ]
      }
      var optionBar = {
        grid: {
          left: '3%',
          right: '3%',
          bottom: 25,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          feature: {
            magicType: { show: true, type: ['line', 'bar'] },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        legend: {
          data: ['用户数量', '物品数量']
        },
        xAxis: [
          {
            type: 'category',
            data: ['2022-1', '2022-2', '2022-3', '2022-4', '2022-5', '2022-6', '2022-7', '2022-8', '2022-9', '2022-10', '2022-11', '2022-2'],
            axisPointer: {
              type: 'shadow'
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '用户数量',
            type: 'bar',
            tooltip: {
              valueFormatter: function (value) {
                return value
              }
            },
            itemStyle: {
              normal: {
                color: '#6CB9F0'
              }
            },
            data: [
              2, 4, 7, 23, 25, 76, 135, 162, 220, 295, 400, 600
            ]
          },
          {
            name: '物品数量',
            type: 'line',
            itemStyle: {
              normal: {
                color: '#2EC7C9'
              }
            },
            tooltip: {
              valueFormatter: function(value) {
                return value
              }
            },
            data: [2, 50, 151, 250, 452, 555, 677, 801, 1002, 1521, 1700, 1900]
          }
        ]
      }
      var optionPie = {
        color: ['#fc8251', '#5470c6', '#91cd77', '#ef6567', '#f9c956', '#75bedc'],
        title: {
          text: '分类统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          left: 'center',
          bottom: '10',
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: '数码家电' },
              { value: 735, name: '日用百货' },
              { value: 580, name: '家具' },
              { value: 484, name: '办公文教' },
              { value: 300, name: '其他' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }

      chartLine.setOption(optionLine)
      charBar.setOption(optionBar)
      charPie.setOption(optionPie)
      // 随窗口的大小变化自适应
      window.addEventListener('resize', function() {
        chartLine.resize()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container{

  padding-bottom: 30px;
  width: 100%;
  height: auto;
  background: #F0F0F0;
}
.char-line{
  margin: 0px 27px 30px 27px;
  width: 97%;
  height: 22rem;
  background: #FFFFFF;
  border-radius: 10px;
}
.char-ano{
  margin: 0px 27px 30px 27px;
  width: 97%;
  height: 21rem;
  //background: #FFFFFF;
  border-radius: 10px;
}
.char-bar{
  float: left;
  height: 100%;
  width: 65%;
  background: #FFFFFF;
  border-radius: 10px;
}
.char-pie{
  float: right;
  height: 100%;
  width: 33.5%;
  background: #FFFFFF;
  border-radius: 10px;
}
.info{
  float: right;
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.info-desc{
  color: #8898aa !important;
  font-size: 18px;
  font-family: inherit;
  font-weight: 600;
  margin: 15px 15px 0px 15px;
}
.info-desc-num{
  font-weight: 600 !important;
  margin-bottom: 0 !important;
  font-size: 1.25rem;
  font-family: Open Sans, sans-serif;
  line-height: 1.5;
  color: #32325d;
}
.el-row {
  padding: 30px 25px;
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple-light {
  background: #e5e9f2;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
