package com.xhj.order.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lyx.common.web.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @Author: xhj
 * @Date: 2023/09/18/17:08
 * @Description:
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "createBy", () -> UserContext.getCurrentUserId(), Long.class);
        this.strictInsertFill(metaObject, "updateBy", () -> UserContext.getCurrentUserId(), Long.class);
        System.out.println(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
    }
}
