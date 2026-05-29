package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.UpdateNoteAllowException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void saveSetmealWithDish(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insert(setmeal);

        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });

            setmealDishMapper.insert(setmealDishes);
        }


    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void deleteSetmealWithDish(List<Long> ids) {

        //起售中的套餐不能删除
        ids.forEach(id ->{
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        setmealDishMapper.deleteBysetmealIds(ids);
        setmealMapper.deleteByIds(ids);

    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {

        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        SetmealDish setmealDish = new SetmealDish();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        List<Long> ids = new ArrayList<>();
        ids.add(setmeal.getId());
        setmealDishMapper.deleteBysetmealIds(ids);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();

        if(status == StatusConstant.ENABLE){
            //查询套餐内所有菜品，如果有停售菜品->抛出异常
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
            setmealDishes.forEach( setmealDish ->{
                Dish dish = dishMapper.getById(setmealDish.getDishId());
                if(dish.getStatus() == StatusConstant.DISABLE){
                    throw new UpdateNoteAllowException(MessageConstant.SETMEAL_BE_RELATED_BY_DISH_ON_SALE);
                }
            });
        }

        setmealMapper.update(setmeal);
    }
}
