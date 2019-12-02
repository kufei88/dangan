package com.rafa.dangan.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019/05/09
 */
public interface DanganMapper {
  @Select("select top ${pageNumber} ztm as 姓名,zrz as 工作单位,cbdh as 身份证,dh+'.pdf' as pdf from 卷内目录" +
    " where dh not in (select top ${page} DH from 卷内目录 order by dh)  order by dh")
  List<Map<String,Object>> getDanganList(Map<String,Object> paramMap);
  @Select("select count(*) from 卷内目录")
  int getDangListCount();

  @Select("select  ztm as 姓名,zrz as 工作单位,cbdh as 身份证,dh+'.pdf' as pdf from 卷内目录" +
    " where ztm like ${param1} or cbdh like ${param2} or zrz like ${param3}")
  List<Map<String,Object>> getDanganListBySearch(Map<String,Object> paramMap);
}
