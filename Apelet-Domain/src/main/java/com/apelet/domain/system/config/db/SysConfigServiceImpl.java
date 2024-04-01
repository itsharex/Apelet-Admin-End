package com.apelet.domain.system.config.db;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author xiaoyuan-zs
 * @since 2022-06-09
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements
    SysConfigService {

    @Override
    public String getConfigValueByKey(String key) {
        if (StrUtil.isBlank(key)) {
            return StrUtil.EMPTY;
        }
        LambdaQueryWrapper<SysConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfigEntity::getConfigKey, key);
        SysConfigEntity one = this.getOne(queryWrapper);
        if (one == null || one.getConfigValue() == null) {
            return StrUtil.EMPTY;
        }
        return one.getConfigValue();
    }


}
