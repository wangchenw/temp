package com.utils.operatemysql.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/3/22 22:39
 */
@Component
public class MybastisAutoHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "createdAt", LocalDateTime.now());
        this.fillStrategy(metaObject, "updatedAt", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "updatedAt", LocalDateTime.now());

    }
}
