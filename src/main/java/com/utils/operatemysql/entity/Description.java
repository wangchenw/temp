package com.utils.operatemysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName description
 */
@TableName(value ="description")
@Data
public class Description implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 
     */
    private String description;
    private String translation;

    /**
     * 
     */
    private String keyword;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String url;

    private String search;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}