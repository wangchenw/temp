package com.utils.operatemysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName sheet1
 */
@TableName(value ="sheet1")
@Data
public class Sheet1 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String characteristic;

    /**
     * 
     */
    private String platform;

    /**
     * 
     */
    private String ip;

    /**
     * 
     */
    private Integer port;

    /**
     * 
     */
    private String property;

    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private String asMassage;

    /**
     * 
     */
    private String whoisMessage;

    /**
     * 
     */
    private String ipuser;

    /**
     * 
     */
    private String certificationUrl;

    /**
     * 
     */
    private String screenshot;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String translation;

    /**
     * 
     */
    private String version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}