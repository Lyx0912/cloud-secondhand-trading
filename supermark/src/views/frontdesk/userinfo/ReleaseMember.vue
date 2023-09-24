<template>
    <div>

        <div style="margin-top: 2rem;margin-bottom: 2rem;">
            <a style="margin-left: 30px;" href="javascript:history.back (-1)" class="el-dropdown-link">
                <i class="el-icon-arrow-left"></i>返回
            </a>
        </div>
        <div class="infinite-list" v-infinite-scroll="load" style="overflow:auto;height: 34rem;" >
            <div>

                <div style="margin-left: 4rem;margin-right: 2rem;height: 18rem;">
                    <div style="height: 15rem;width: 100%;">
                        <img src="" style="width: 15rem;height: 15rem;float: left;margin: 15px;">
                        <div style="float: left;margin-top: 1rem;margin-left: 1rem;">
                            <div style="float: left;font-size: 1.5rem;">华为P60 8+128G</div>
                            <div style="float: right;width: 10rem;font-size: 1.2rem;">价格：<span
                                    style="color:rgb(234, 12, 12);font-size: 1.4rem;">¥ 9999</span></div>
                            <div style="height: 1rem;"></div>
                            <div style="clear: left;width: 55rem;clear: left;margin-top: 3rem;height: 2.4rem;">
                                <div><span>描述：</span><span
                                        style="color: #717171;font-size: 15px;">老婆不让干了，小几万处理！双胞胎原装台湾丽伟750加工中心
                                        发那科0i-md系统、阿尔法大驱动、BT40主轴10000转、刀臂式刀库、三轴线轨、重型机、带螺杆式排削机、精度一个丝左右</span>
                                </div>
                            </div>
                            <!-- <span v-for="img in images" :key="img.id">
                            <el-image style="width: 180px;height: 180px;" alt="无法显示" :src="img.url"
                                :preview-src-list="image" v-if="[[images != null]]" />
                        </span> -->
                            <div style="height: 1rem;"></div>
                            <div style="clear: left;">
                                <img src="" style="width: 6rem;height: 6rem;float: left;margin: 15px;">
                                <img src="" style="width: 6rem;height: 6rem;float: left;margin: 15px;">
                                <img src="" style="width: 6rem;height: 6rem;float: left;margin: 15px;">
                            </div>
                            <div style="height: 2rem;clear: left;"></div>

                        </div>
                    </div>
                </div>
                <div>
                </div>
                <div style="float: left;margin-left: 30px;">
                    <template>
                        <el-table v-loading="listLoading" :row-style="{ height: '45px' }" :cell-style="{ padding: '0px' }"
                            :data="list" element-loading-text="Loading" border fit highlight-current-row>
                            <el-table-column align="center" label="买家名称" prop="consignee" width="120 "
                                :show-overflow-tooltip="true" />
                            <el-table-column align="center" label="名字" prop="consignee" width="120 "
                                :show-overflow-tooltip="true" />
                            <el-table-column align="center" label="省" prop="province" width="90"
                                :show-overflow-tooltip="true" />
                            <el-table-column align="center" label="市" prop="city" width="100"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="区\县" align="center" prop="area" width="100" />
                            <el-table-column label="详细地址" align="center" prop="addr" width="150"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="付款金额" align="center" prop="mobile" width="150"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="购买时间" align="center" prop="payAmount" width="150"
                                :show-overflow-tooltip="true" />
                            <el-table-column label="付款时间" align="center" prop="createTime" width="150"
                                :show-overflow-tooltip="true" />
                            <el-table-column align="center" prop="created_at" label="操作" width="130">
                                <template v-slot="scope">
                                    <el-button type="primary" icon="el-icon-edit" size="mini"
                                        @click="updateAddr(scope.row.addrId)">填写快递单号</el-button>
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                            :current-page="queryInfo.pagenum" :page-sizes="[5, 10, 15]" :page-size="queryInfo.pagesize"
                            layout="total, sizes, prev, pager, next, jumper" :total="total">
                        </el-pagination>
                    </template>

                </div>
            </div>
        </div>
        <el-dialog :title="goodsFormTitle" :visible.sync="goodsFormShow" width="35%">
            <div class="aaa" :id="list.addrId" style="border: solid #727272 1px;width: 100%;height: 80px;margin-top: 20px;"
                v-for="(list, index) in list" :key="index" @click="changeclass(list.addrId, index)">
                <div style="height: 10px;"></div>
                <div style="margin-left: 2%;float: left;">
                    <span>{{ list.consignee }}</span>
                    <span style="margin-left: 20px;">{{ list.mobile.replace(/^(\d{3})\d{4}(\d+)/, '$1****$2') }}</span>
                    <el-tag id="acquiesces" v-if="list.isDefault == 1" type="danger" size="mini"
                        style="margin-left: 50px; ">默认地址</el-tag>
                    <div style="height: 10px;"></div>
                    <div>{{ list.province }} {{ list.city }} {{ list.area }} {{ list.addr }} </div>
                </div>
            </div>
            <div style="clear: left;" slot="footer" class="dialog-footer">
                <el-button @click="closeDialog">取 消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
 
<script>
import ClipboardJS from 'clipboard';
export default {
    mounted() {

    },
    data() {
        return {
            status: 1,
            addr: {},
            danhao: 'SF93284374387593',
            goodsFormTitle: '修改地址',
            goodsFormShow: false,
            goodsForm: {},
            goodsId: 0,
            List: [],
            addrId: 0,
            index: 0,
            listLoading: false,
            // 定义数据源
            List: [],
            //查询分页信息
            queryInfo: {
                uid: JSON.parse(sessionStorage.getItem("user")).uid,
                pagenum: 1,
                pagesize: 5,
            },
        }
    },
    created() {
        this.master = JSON.parse(sessionStorage.getItem("user")).username;
        if (this.$route.query.goodsId != undefined) {
            this.goodsId = this.$route.query.goodsId;
        }
        this.getList()
        this.getOrderAddrs()
    },
    methods: {
        deleteById(orderSn, goodsId) {
            console.log(orderSn)
            console.log(goodsId)
            this.$confirm('是否取消该订单?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var memberId = JSON.parse(sessionStorage.getItem("user")).uid
                this.$http.delete('/api/cloud-order/order/deleteOrderByOrderId',
                    {
                        params: { 'orderSn': orderSn, 'memberId': memberId, 'goodsId': goodsId }
                    }).then(res => {
                        this.$message.success('取消订单成功！')
                        this.getList()
                    });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        payment(orderSn) {
            // window.location.href("/checkstand/"+orderSn)
            this.$router.push({ path: "/checkstand", query: { 'orderSn': orderSn } });
        },
        save() {
            console.log(this.list[this.index])
            if (this.addr != 0) {
                this.$http.post("/api/cloud-order/orderaddr/updates", { id: this.goodsForm.orderAddr.id, orderAddr: this.list[this.index] }).then(res => {
                    this.goodsFormShow = false;
                    this.getList()
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                });
            }
        },
        changeclass(addrId, index) {
            this.addrId = addrId
            this.index = index
            console.log(this.index)
            var id = document.getElementById(addrId + "")
            var cla = document.querySelector(".aaa")
            cla.style.border = 'solid #727272 1px';
            id.style.border = 'solid chartreuse 1px';
            for (var i = 0; i < this.list.length; i++) {
                if (this.list[i].addrId != addrId) {
                    var vid = document.getElementById(this.list[i].addrId + "")
                    vid.style.border = 'solid #727272 1px';
                    console.log(this.list[i].addrId)
                }
            }
        },
        getOrderAddrs() {
            var memberId = JSON.parse(sessionStorage.getItem("user_id"));
            this.$http.get("/api/cloud-member/memberAddr/list?pageNo=1&pageSize=10&memberId=" + memberId).then(res => {
                console.log(res);
                this.list = res.data.data.list;
                console.log("addrs" + this.list);
            });
        },
        closeDialog() { this.goodsFormShow = false; this.goodsFrom = {} },
        getList() {
            var memberId = JSON.parse(sessionStorage.getItem("user_id"));
            console.log(this.orderId)
            this.$http.get("/api/cloud-order/order/getGoods", { praams: { memberId: memberId, goodsId: this.goodsId } }
            ).then(res => {
                this.goodsForm = res.data.data
                this.status = this.goodsForm.order.state
                console.log(this.goodsForm)
                console.log(this.goodsForm.orderAddr)
                console.log(this.goodsForm.orderGoodsDTO)
            });
        },
        updateAddr(addrId) {
            this.getOrderAddrs()
            this.goodsFormShow = true;
        },
        copy() {
            new ClipboardJS('.copy-btn');
            this.$message({
                message: '复制成功',
                type: 'success'
            });
        }
    }
}
</script>
<style></style>

