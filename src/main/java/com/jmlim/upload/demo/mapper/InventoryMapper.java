package com.jmlim.upload.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface InventoryMapper {
   // void insertInventory(Map<String, Object> param);

    void insertInventory(@Param("paramList") List<Map<String, Object>> paramList);
}
