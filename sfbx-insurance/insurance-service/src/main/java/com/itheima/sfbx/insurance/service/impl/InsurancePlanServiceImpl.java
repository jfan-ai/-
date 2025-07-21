package com.itheima.sfbx.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.sfbx.framework.commons.exception.ProjectException;
import com.itheima.sfbx.framework.commons.utils.BeanConv;
import com.itheima.sfbx.framework.commons.utils.EmptyUtil;
import com.itheima.sfbx.framework.commons.utils.ExceptionsUtil;
import com.itheima.sfbx.insurance.constant.InsurancePlanCacheConstant;
import com.itheima.sfbx.insurance.dto.InsurancePlanVO;
import com.itheima.sfbx.insurance.dto.PlanEarningsVO;
import com.itheima.sfbx.insurance.dto.PlanSafeguardVO;
import com.itheima.sfbx.insurance.enums.InsurancePlanEnum;
import com.itheima.sfbx.insurance.mapper.InsurancePlanMapper;
import com.itheima.sfbx.insurance.pojo.InsurancePlan;
import com.itheima.sfbx.insurance.pojo.PlanEarnings;
import com.itheima.sfbx.insurance.pojo.PlanSafeguard;
import com.itheima.sfbx.insurance.service.IInsurancePlanService;
import com.itheima.sfbx.insurance.service.IPlanEarningsService;
import com.itheima.sfbx.insurance.service.IPlanSafeguardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * @Description：保险方案服务实现类
 */
@Slf4j
@Service
public class InsurancePlanServiceImpl extends ServiceImpl<InsurancePlanMapper, InsurancePlan> implements IInsurancePlanService {

    @Autowired
    IPlanSafeguardService planSafeguardService;

    @Autowired
    IPlanEarningsService planEarningsService;

    @Autowired
    IdentifierGenerator identifierGenerator;


    /***
    * @description 保险方案多条件组合
    * @param insurancePlanVO 保险方案
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsurancePlan> queryWrapper(InsurancePlanVO insurancePlanVO){
        QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
        //保险商品id查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsurancePlan::getInsuranceId,insurancePlanVO.getInsuranceId());
        }
        //保险商品ids列表查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getInsuranceIds())) {
            queryWrapper.lambda().in(InsurancePlan::getInsuranceId,insurancePlanVO.getInsuranceIds());
        }
        //保险计划名称查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPalnName())) {
            queryWrapper.lambda().eq(InsurancePlan::getPalnName,insurancePlanVO.getPalnName());
        }
        //默认定价查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPrice())) {
            queryWrapper.lambda().eq(InsurancePlan::getPrice,insurancePlanVO.getPrice());
        }
        //默认定价单位：y/d,y/m,y/y查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPriceUnit())) {
            queryWrapper.lambda().eq(InsurancePlan::getPriceUnit,insurancePlanVO.getPriceUnit());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getSortNo())) {
            queryWrapper.lambda().eq(InsurancePlan::getSortNo,insurancePlanVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getDataState())) {
            queryWrapper.lambda().eq(InsurancePlan::getDataState,insurancePlanVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsurancePlan::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insurancePlanVO.hashCode()")
    public Page<InsurancePlanVO> findPage(InsurancePlanVO insurancePlanVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsurancePlan> InsurancePlanPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsurancePlan> queryWrapper = queryWrapper(insurancePlanVO);
            //执行分页查询
            Page<InsurancePlanVO> insurancePlanVOPage = BeanConv.toPage(page(InsurancePlanPage, queryWrapper), InsurancePlanVO.class);
            if (!EmptyUtil.isNullOrEmpty(insurancePlanVOPage.getRecords())){
                //补全方案保障项、给付计划
                Set<Long> planIds = insurancePlanVOPage.getRecords().stream().map(InsurancePlanVO::getId).collect(Collectors.toSet());
                //保障项
                List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findInPlanId(planIds);
                List<PlanSafeguardVO> planSafeguardVOsHandler = Lists.newArrayList();
                //给付计划
                List<PlanEarningsVO> planEarningsVOs = planEarningsService.findInPlanId(planIds);
                insurancePlanVOPage.getRecords().forEach(n->{
                    planSafeguardVOs.forEach(i->{
                        if (n.getId().equals(i.getPlanId())) {
                            planSafeguardVOsHandler.add(i);
                        }
                    });
                    n.setPlanSafeguardVOs(planSafeguardVOsHandler);
                    planEarningsVOs.forEach(j->{
                        if (n.getId().equals(j.getPalnId())) {
                            n.setPlanEarningsVO(j);
                        }
                    });
                });
            }
            //返回结果
            return insurancePlanVOPage;
        }catch (Exception e){
            log.error("保险方案分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.BASIC,key ="#insurancePlanId")
    public InsurancePlanVO findByIdAndInsuranceId(Long insurancePlanId,Long insuranceId){
        try {
            //执行查询
            QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InsurancePlan::getId,insurancePlanId).eq(InsurancePlan::getInsuranceId,insuranceId);
            InsurancePlanVO insurancePlanVO = BeanConv.toBean(getOne(queryWrapper), InsurancePlanVO.class);
            //方案保障项
            List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findByPlanId(insurancePlanVO.getId());
            insurancePlanVO.setPlanSafeguardVOs(planSafeguardVOs);
            //方案给付计划
            PlanEarningsVO planEarningsVO =planEarningsService.findByPlanId(insurancePlanVO.getId());
            insurancePlanVO.setPlanEarningsVO(planEarningsVO);
            return insurancePlanVO;
        }catch (Exception e){
            log.error("保险方案单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
                    @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true),
            }
    )
    public Boolean save(List<InsurancePlanVO> insurancePlanVOs) {
        try {
            //最终操作结果
            boolean flag = false;
            //1.批量保存保险方案
            //给所有的保险方案设置方案编号
            insurancePlanVOs.forEach(insurancePlanVO -> insurancePlanVO.setPlanNo(identifierGenerator.nextId(insurancePlanVO).longValue()));
            //转化为vo实体
            List<InsurancePlan> insurancePlans = BeanConv.toBeanList(insurancePlanVOs, InsurancePlan.class);
            //保存
            flag = saveBatch(insurancePlans);
            //因为保存方案后，方案的id在insurancePlans里面；insurancePlanVOs的每个方案是没有方案id的
            //所以可以将方案编号与保险方案id进行关联
            //Map<方案编号,方案id>
            Map<Long, Long> planNoAndIdMap = insurancePlans.stream().collect(Collectors.toMap(InsurancePlan::getPlanNo, InsurancePlan::getId));


            //2.批量保存每个方案的保障项
            //给方案保障项设置方案ID
            insurancePlanVOs.forEach(
                    insurancePlanVO -> {
                        insurancePlanVO.getPlanSafeguardVOs().forEach(
                                planSafeguardVO -> planSafeguardVO.setPlanId(planNoAndIdMap.get(insurancePlanVO.getPlanNo()))
                        );
                        List<PlanSafeguard> planSafeguardList = BeanConv.toBeanList(insurancePlanVO.getPlanSafeguardVOs(), PlanSafeguard.class);
                        planSafeguardService.saveBatch(planSafeguardList);
                    }

            );


            //3.批量保存每个方案的给付计划
            insurancePlanVOs.forEach(
                    insurancePlanVO -> {
                        if(insurancePlanVO.getPlanEarningsVO()!=null){
                            insurancePlanVO.getPlanEarningsVO().setPalnId(insurancePlanVO.getId());
                            planEarningsService.save(insurancePlanVO.getPlanEarningsVO());
                        }
                    }
            );
            return flag;
        } catch (Exception e) {
            log.error("批量保存方案失败：{}",ExceptionsUtil.getStackTraceAsString( e));
            throw new ProjectException(InsurancePlanEnum.SAVE_FAIL);
        }
    }


    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true)})
    public Boolean update(List<InsurancePlanVO> insurancePlanVOs) {
        try {
            //查询原产品方案
            QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InsurancePlan::getInsuranceId,insurancePlanVOs.get(0).getInsuranceId());
            List<InsurancePlan> insurancePlans = list(queryWrapper);
            //删除原产品方案
            List<Long> insurancePlanIds = insurancePlans.stream().map(InsurancePlan::getId).collect(Collectors.toList());
            boolean flag = removeByIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除保险方案失败");
            }
            //删除原产品方案保障项
            flag = planSafeguardService.deleteInPlanIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除方案保障项失败");
            }
            //删除原产品方案给付计划
            planEarningsService.deleteInPlanIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除方案给付计划失败");
            }
            //清空产品方案ID
            insurancePlanVOs.forEach(n->n.setId(null));
            //再次重新保存
            return this.save(insurancePlanVOs);
        }catch (Exception e){
            log.error("修改保险方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案失败");
            }
            flag = planSafeguardService.deleteInPlanId(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案保障项失败");
            }
            flag = planEarningsService.deleteInPlanId(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案给付计划失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.LIST,key ="#insurancePlanVO.hashCode()")
    public List<InsurancePlanVO> findList(InsurancePlanVO insurancePlanVO) {
        try {
            //构建查询条件
            QueryWrapper<InsurancePlan> queryWrapper = queryWrapper(insurancePlanVO);
            //执行列表查询
            List<InsurancePlanVO> insurancePlanVOs = BeanConv.toBeanList(list(queryWrapper),InsurancePlanVO.class);
            if (!EmptyUtil.isNullOrEmpty(insurancePlanVOs)){
                //补全方案保障项、给付计划
                Set<Long> planIds = insurancePlanVOs.stream().map(InsurancePlanVO::getId).collect(Collectors.toSet());
                //保障项
                List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findInPlanId(planIds);
                //给付计划
                List<PlanEarningsVO> planEarningsVOs = planEarningsService.findInPlanId(planIds);
                insurancePlanVOs.forEach(n->{
                    List<PlanSafeguardVO> planSafeguardVOsHandler = Lists.newArrayList();
                    planSafeguardVOs.forEach(i->{
                        if (n.getId().equals(i.getPlanId())) {
                            planSafeguardVOsHandler.add(i);
                        }
                    });
                    n.setPlanSafeguardVOs(planSafeguardVOsHandler);
                    planEarningsVOs.forEach(j->{
                        if (n.getId().equals(j.getPalnId())) {
                            n.setPlanEarningsVO(j);
                        }
                    });
                });
            }
            return insurancePlanVOs;
        }catch (Exception e){
            log.error("保险方案列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.LIST_FAIL);
        }
    }

}
