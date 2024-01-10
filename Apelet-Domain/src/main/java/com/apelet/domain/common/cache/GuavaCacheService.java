package com.apelet.domain.common.cache;


import com.apelet.domain.system.config.db.SysConfigService;
import com.apelet.domain.system.dept.db.SysDeptEntity;
import com.apelet.domain.system.dept.db.SysDeptService;
import com.apelet.framework.cache.guava.AbstractGuavaCacheTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 根据实际使用情况设置对应的guava缓存，通过使用AbstractGuavaCacheTemplate抽象类查询对应数据实现二级缓存
 * 数据量小，且访问频繁，或者说一些不会变的静态配置数据，都可以放到本地缓存中
 * @author xiaoyuan-zs
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class GuavaCacheService {

    private final SysConfigService configService;

    private final SysDeptService deptService;

    public final AbstractGuavaCacheTemplate<String> configCache = new AbstractGuavaCacheTemplate<String>() {
        @Override
        public String getObjectFromDb(Object id) {
            return configService.getConfigValueByKey(id.toString());
        }
    };

    public final AbstractGuavaCacheTemplate<SysDeptEntity> deptCache = new AbstractGuavaCacheTemplate<SysDeptEntity>() {
        @Override
        public SysDeptEntity getObjectFromDb(Object id) {
            return deptService.getById(id.toString());
        }
    };


}
