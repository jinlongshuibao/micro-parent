package com.uiotsoft.micro.oauth2.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * 2018/2/3
 * <p>
 * Replace decorator.xml
 * <p>
 * Sitemesh
 *
 * @author Shengzhao Li
 */
public class SOSSiteMeshFilter extends ConfigurableSiteMeshFilter {


    public SOSSiteMeshFilter() {
    }


    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

        builder.addDecoratorPath("/*", "/main")
                .addExcludedPath("/static/**")
                .addExcludedPath("/resources/**");;


    }
}
