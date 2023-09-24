<template>
    <div>
        <div style="margin-top: 2rem;margin-bottom: 3rem;">
            <a style="margin-left: 30px;" href="javascript:history.back (-1)" class="el-dropdown-link">
                <i class="el-icon-arrow-left"></i>返回
            </a>
        </div>
        <div>
            <el-steps v-if="status != 4" :active="status + 1" align-center finish-status="success">
                <el-step title="未付款" description="可取消"></el-step>
                <el-step title="已付款" description="可取消"></el-step>
                <el-step title="已发货" description="发货之后不可取消"></el-step>
                <el-step title="已完成" description=""></el-step>
            </el-steps>
            <el-steps v-if="status == 4" :active="1" align-center finish-status="error ">
                <el-step icon="el-icon-warning" title="已取消" description="订单已取消"></el-step>
            </el-steps>
        </div>
        <div>
            <div style="margin-bottom: 2rem;margin-top: 2rem;margin-left: 3rem;font-size: 1.2rem;">
                订单号：{{ this.goodsForm.order.orderSn }}
            </div>
            <div style="float: left;">
                <div style="height: 22rem;border: solid rgb(238, 238, 238) 1px;width: 39rem;">
                    <img :src="this.goodsForm.orderGoodsDTO.defaultImg"
                        style="width: 120px;height: 120px;float: left;margin: 15px;">
                    <div style="margin-top: 20px;">
                        <div style="float: left;width: 50%;">{{ this.goodsForm.orderGoodsDTO.name }}</div>
                        <div style="float: left;width: 5%;">×1</div>
                        <div style="float: left;width: 15%;">¥ {{ this.goodsForm.orderGoodsDTO.price }}</div>
                        <div style="height: 2rem;clear: left;"></div>
                        <div style="clear: left;margin-left: 1rem;margin-right: 1rem;">
                            <div><span>描述：</span><span style="color: #717171;font-size: 15px;">{{
                                this.goodsForm.orderGoodsDTO.description }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>


            </div>
            <div style="float: left;margin-left: 30px;">
                <div>
                    <div style="float: left;">收货信息：</div>
                    <div style="float: right;margin-right: 2rem;"><el-button plain @click="updateAddr(1)">修改收货地址</el-button>
                    </div>
                </div>
                <div style="height: 10px;"></div>
                <div style="margin-top: 40px;">
                    <div>
                        <span>姓 名：</span>
                        <span>{{ this.goodsForm.orderAddr.consignee }}</span>
                    </div>
                    <div style="height: 10px;"></div>
                    <div>
                        <span>收货地址：</span>
                        <span>{{ this.goodsForm.orderAddr.province }} {{ this.goodsForm.orderAddr.city }} {{
                            this.goodsForm.orderAddr.area }} {{ this.goodsForm.orderAddr.addr }}</span>
                    </div>
                </div>
                <div style="height: 10px;"></div>
                <div style="margin-top: 40px;">
                    <div style="float: left;width: 20rem;">
                        <span>创建时间：</span>
                        <span>{{ this.goodsForm.order.createTime }}</span>
                    </div>
                    <div style="float: right;width: 20rem;">
                        <span>付款时间：</span>
                        <span>{{ this.goodsForm.order.payTime }}</span>
                    </div>
                </div>
                <div style="height: 50px;"></div>
                <div style="">
                    <div style="float: left;width: 20rem;">
                        <span>发货时间：</span>
                        <span>{{ this.goodsForm.order.deliveryTime }}</span>
                    </div>
                    <div style="float: right;width: 20rem;">
                        <span>完成时间：</span>
                        <span>{{ this.goodsForm.order.finallyTime }}</span>
                    </div>
                </div>
                <div style="height: 50px;"></div>
                <div style="">
                    <div style="float: left;width: 20rem;">
                        <span>支付方式：</span>
                        <span v-if="goodsForm.order.payType == 1">支付宝支付</span>
                        <span v-if="goodsForm.order.payType == 2">微信支付</span>
                        <span v-if="goodsForm.order.payType == 3">花呗支付</span>
                    </div>
                    <div style="float: right;width: 20rem;">
                        <span>支付流水号：</span>
                        <span v-if="goodsForm.order.isPayed==1">0000000000000000000</span>
                    </div>
                </div>
                <div style="height: 50px;"></div>
                <div style="">
                    <div v-if="status == 2 || status == 3" style="float: left;width: 20rem;">
                        <span>快递单号：</span>
                        <span class="copy-btn" :data-clipboard-text="danhao">{{ danhao }}</span>
                        <span style="margin-left: 10px;" @click="copy" class="el-icon-document-copy"></span>
                    </div>
                    <div style="float: right;margin-right: 36px;">
                        <el-button v-if="goodsForm.order.state == 0" type="primary" @click="payment(danhao)">付款</el-button>
                        <el-button v-if="goodsForm.order.state ==0||goodsForm.order.state ==1" type="danger"
                            @click="deleteById(goodsForm.order.orderSn, goodsForm.order.goodsId)">取消</el-button>
                        <el-button type="success"
                            v-if="goodsForm.order.state == 2 && goodsForm.order.confirmStatus == 0">确认收货</el-button>
                    </div>
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
            orderId: 0,
            List: [],
            addrId: 0,
            index: 0
        }
    },
    created() {
        this.master = JSON.parse(sessionStorage.getItem("user")).username;
        if (this.$route.query.orderId != undefined) {
            this.orderId = this.$route.query.orderId;
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
            console.log(this.orderId)
            this.$http.get("/api/cloud-order/order/info/" + this.orderId,
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

