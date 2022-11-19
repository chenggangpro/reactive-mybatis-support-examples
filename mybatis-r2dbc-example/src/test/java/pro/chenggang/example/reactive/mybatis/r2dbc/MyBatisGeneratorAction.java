package pro.chenggang.example.reactive.mybatis.r2dbc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pro.chenggang.project.reactive.mybatis.support.generator.core.MybatisDynamicCodeGenerator;

/**
 * @author Gang Cheng
 * @date 2021-12-26.
 */
public class MyBatisGeneratorAction {

    @Disabled
    @Test
    public void generate(){
        MybatisDynamicCodeGenerator.withYamlConfiguration()
                .customConfigure()
                .applyGenerateBasePackageFromClass(MyBatisGeneratorAction.class)
                .customizeGeneratorProperties()
                .targetPackageBuilder()
                .basePackage("pro.chenggang.example.reactive.mybatis.r2dbc")
                .thenPropertiesBuilder()
                .thenConfigurer()
                .toGenerator()
                .generate();
    }

}
