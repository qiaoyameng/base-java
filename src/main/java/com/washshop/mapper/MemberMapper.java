package com.washshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.washshop.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    @Select("SELECT * FROM member WHERE user_id = #{userId} AND deleted = 0")
    Member selectByUserId(@Param("userId") Long userId);

    @Update("UPDATE member SET points = points + #{points} WHERE user_id = #{userId}")
    int addPoints(@Param("userId") Long userId, @Param("points") Integer points);

    @Update("UPDATE member SET points = points - #{points} WHERE user_id = #{userId} AND points >= #{points}")
    int deductPoints(@Param("userId") Long userId, @Param("points") Integer points);

    @Update("UPDATE member SET balance = balance + #{amount} WHERE user_id = #{userId}")
    int addBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);

    @Update("UPDATE member SET balance = balance - #{amount} WHERE user_id = #{userId} AND balance >= #{amount}")
    int deductBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
