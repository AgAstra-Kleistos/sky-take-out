package com.sky.mapper;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量添加口味
     * @param flavors
     */
    @AutoFill(value = OperationType.INSERT)
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id集合批量删除口味
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询风味
     * @return
     */
    @Select("Select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
