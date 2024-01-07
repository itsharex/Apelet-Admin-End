package com.apelet.integrationTest.db;

import com.apelet.common.enums.common.ConfigKeyEnum;
import com.apelet.domain.system.config.db.SysConfigService;
import com.apelet.integrationTest.IntegrationTestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = IntegrationTestApplication.class)
@RunWith(SpringRunner.class)
class  SysConfigServiceImplTest {

    @Resource
    SysConfigService configService;

    @Test
    void testGetConfigValueByKey() {
        String configValue = configService.getConfigValueByKey(ConfigKeyEnum.CAPTCHA.getValue());
        Assertions.assertFalse(Boolean.parseBoolean(configValue));
    }


}
