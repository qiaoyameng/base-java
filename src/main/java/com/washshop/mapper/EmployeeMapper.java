package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("SELECT * FROM employee WHERE store_id = #{storeId} AND deleted = 0")
    List<Employee> selectByStoreId(@Param("storeId") Long storeId);

    @Select("SELECT * FROM employee WHERE user_id = #{userId} AND deleted = 0")
    Employee selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM employee WHERE employee_no = #{employeeNo} AND deleted = 0")
    Employee selectByEmployeeNo(@Param("employeeNo") String employeeNo);
}
