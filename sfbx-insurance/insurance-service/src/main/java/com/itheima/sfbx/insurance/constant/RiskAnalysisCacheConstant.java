package com.itheima.sfbx.insurance.constant;

import com.itheima.sfbx.framework.commons.constant.basic.CacheConstant;

/**
* @Description：风险类目缓存常量
*/
public class RiskAnalysisCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "risk-analysis:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";


}
