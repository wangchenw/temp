package com.utils.operatemysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName nofound
 */
@TableName(value ="nofound")
@Data
public class Nofound implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String text;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}