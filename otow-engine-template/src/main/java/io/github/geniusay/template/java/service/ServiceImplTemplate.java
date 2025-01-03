package io.github.geniusay.template.java.service;

import io.github.geniusay.template.java.ClassTemplate;
import io.github.geniusay.template.meta.MetaAnnotation;
import org.springframework.stereotype.Service;

public class ServiceImplTemplate extends ClassTemplate {

    private ServiceTemplate serviceTemplate;


    public ServiceImplTemplate(String packagePath, String className) {
        super(packagePath, className+"ServiceImpl");
    }

    public ServiceImplTemplate(String packagePath, String className, ServiceTemplate serviceTemplate) {
        super(packagePath, className+"ServiceImpl");
        setServiceTemplate(serviceTemplate);
    }

    public void setServiceTemplate(ServiceTemplate serviceTemplate){
        this.serviceTemplate = serviceTemplate;
        this.addInterfaces(serviceTemplate.getClassName(), serviceTemplate.getAllPackagePath());
    }

    @Override
    public void initAnnotations() {
        addAnnotation(new MetaAnnotation(Service.class));
    }
}
