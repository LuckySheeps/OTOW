package io.github.geniusay.velocity;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.github.geniusay.engine.VelocityCodeEngine;
import io.github.geniusay.template.java.ApplicationConfigTemplate;
import io.github.geniusay.template.java.PomConfigTemplate;
import io.github.geniusay.template.java.method.*;
import io.github.geniusay.template.java.service.*;
import io.github.geniusay.template.meta.MetaAnnotation;
import io.github.geniusay.template.meta.MetaMethod;
import io.github.geniusay.template.meta.MetaMethodParam;
import io.github.geniusay.template.meta.MetalField;
import io.github.geniusay.template.java.model.*;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.github.geniusay.common.constant.TemplateConstant.REQUEST_BODY;

public class VelocityGenerateTest {

    private static VelocityCodeEngine engine = VelocityCodeEngine.getCodeEngine();
    private static EntityTemplate getEntityTemplate() {
        EntityTemplate userEntity = new EntityTemplate("io.github.geniusay.velocity.generate", "User", "user");

        MetaAnnotation fillAnnotation = new MetaAnnotation(TableField.class, "fill", "FieldFill.INSERT_UPDATE", FieldFill.class);
        MetaAnnotation tableId = new MetaAnnotation(TableId.class);

        userEntity.addModelFields(List.of(
                new MetalField("id", String.class, tableId),
                new MetalField("uid", String.class),
                new MetalField("uid", String.class),
                new MetalField("avatar", String.class),
                new MetalField("createTime", Date.class, fillAnnotation),
                new MetalField("updateTime", Date.class, fillAnnotation)
        ));
        return userEntity;
    }

    private static ServiceTemplate getServiceTemplate() {
        ServiceTemplate userService = new ServiceTemplate("io.github.geniusay.velocity.generate.service", "User");
        EntityTemplate entityTemplate = getEntityTemplate();
        String className = entityTemplate.getClassName();
        userService.addMethod("insertUserInfo",List.of(
            new MetaMethodParam(className, userService.getAllPackagePath(), className.toLowerCase())
        ));
        return userService;
    }

    private static MetaMethod getPostMethod(){
        ServiceTemplate serviceTemplate = getServiceTemplate();
        ReqTemplate reqUser = new ReqTemplate("io.github.geniusay.velocity.generate.pojo.req", "User");
        MetaMethodParam metaMethodParam = new MetaMethodParam(reqUser.getClassName(),reqUser.getAllPackagePath(), reqUser.getClassName().toLowerCase());
        metaMethodParam.addAnnotations(List.of(REQUEST_BODY));

        String body = String.format("%s.%s(%s);", serviceTemplate.getClassName().toLowerCase(), "insertUserInfo", metaMethodParam.getName());
        return new MetaMethod("insertUserInfo", List.of(metaMethodParam), body);
    }

    @Test
    public void generateEntity(){
        EntityTemplate userEntity = getEntityTemplate();

        String generate = engine.generate(userEntity);

        System.out.println(generate);
    }


    @Test
    public void generateController(){
        ControllerTemplate userController = new ControllerTemplate("io.github.geniusay.velocity.generate", "User", "/user");
        ServiceTemplate userService = getServiceTemplate();
        userController.addService(userService);
        userController.addPostMethod(getPostMethod(),"/add/user");
        String generateController = engine.generate(userController);
        String generateService = engine.generate(userService);

        System.out.println(generateController);
        System.out.println(generateService);
    }


    @Test
    public void generateMapper(){
        MapperTemplate mapperTemplate = new MapperTemplate("io.github.geniusay.velocity.generate.mapper","User", getEntityTemplate());

        String generate = engine.generate(mapperTemplate);
        System.out.println(generate);
    }

    @Test
    public void generateServiceImpl(){
        ServiceImplTemplate serviceImplTemplate = new ServiceImplTemplate("io.github.geniusay.velocity.generate.service.impl","User", getServiceTemplate());

        String generate = engine.generate(serviceImplTemplate);
        System.out.println(generate);
    }

    @Test
    public void generateApplication(){
        ApplicationTemplate applicationTemplate = new ApplicationTemplate("io.github.geniusay.velocity.generate","OTOW");

        String generate = engine.generate(applicationTemplate);
        System.out.println(generate);
    }

    // service 的增加方法
    @Test
    public void generateAddService(){
        AddServiceMethodTemplate addServiceMethodTemplate = new AddServiceMethodTemplate("service.method.save.java.vm","User");

        String generate = engine.generate(addServiceMethodTemplate);
        System.out.println(generate);
    }

    // service的删除方法
    @Test
    public void generateDeleteService(){
        DeleteServiceMethodTemplate deleteServiceMethodTemplate = new DeleteServiceMethodTemplate("service.method.delete.java.vm","User");

        String generate = engine.generate(deleteServiceMethodTemplate);
        System.out.println(generate);
    }

    // service的查找方法
    @Test
    public void generateSelectService(){
        SelectServiceMethodTemplate selectServiceMethodTemplate = new SelectServiceMethodTemplate("service.method.select.java.vm","User");

        String generate = engine.generate(selectServiceMethodTemplate);
        System.out.println(generate);
    }

    // service的更新方法
    @Test
    public void generateUpdateService(){
        UpdateServiceMethodTemplate updateServiceMethodTemplate = new UpdateServiceMethodTemplate("service.method.update.java.vm","User");

        String generate = engine.generate(updateServiceMethodTemplate);
        System.out.println(generate);
    }

    @Test
    public void generateApplicationConfig(){
        Map<String, Object> config = Map.of(
                "port", 8080,
                "mysql", Map.of(
                        "enable", true,
                        "url", "jdbc:mysql://127.0.0.1:3306/otow?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC",
                        "username", "root",
                        "password", "root"
                ),
                "applicationName","otow"
        );
        String s = JSONObject.toJSONString(config);
        ApplicationConfigTemplate configTemplate = new ApplicationConfigTemplate(JSONObject.parseObject(s));
        String generate = engine.generate(configTemplate);
        System.out.println(generate);
    }

    @Test
    public void generatePomConfig(){
        Map<String, Object> dependency = Map.of(
                "groupId", "org.springframework.boot",
                "artifactId", "spring-boot-starter-test",
                "version","${boot.version}"
        );

        Map<String, Object> property = Map.of(
                "name", "boot.version",
                "value", "2.3.9.RELEASE"
        );
        PomConfigTemplate configTemplate = new PomConfigTemplate();
        configTemplate.addDependency(dependency);
        configTemplate.addProperties(property);
        String generate = engine.generate(configTemplate);
        System.out.println(generate);
    }

}
