package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    //注入Mapperc层
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {

        //创建对象，转换类型，交换数据
        Category category = new Category();
        //对象属性copy
        BeanUtils.copyProperties(categoryDTO, category);
        //分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);

       /* //补全基础属性
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());*/

        //调用Mapper层方法，实现操作
        categoryMapper.insert(category);

    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {

        /*Category category = categoryMapper.findById(id);

        if(category == null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        };*/

        //关联判断
        Integer count = dishMapper.countByCategoryId(id);

        if(count > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count = setmealMapper.countBycategoryId(id);
        if(count > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        /*category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());*/

        categoryMapper.update(category);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {

        /*Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());*/

        Category category = Category.builder()
                .id(id)
                .status(status)
                //.updateTime(LocalDateTime.now())
                //.updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);

    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据分类查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {

        return categoryMapper.list(type);

    }

}

