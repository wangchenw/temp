package com.utils.operatemysql.mapper;

import com.utils.operatemysql.entity.Description;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author W
* @description 针对表【description】的数据库操作Mapper
* @createDate 2022-04-27 17:00:29
* @Entity com.utils.operatemysql.entity.Description
*/
@Mapper
public interface DescriptionMapper extends BaseMapper<Description> {

}




