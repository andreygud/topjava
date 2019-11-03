package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;

import java.util.Arrays;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        return new String[]{Profiles.getActiveDbProfile(), returnRepositoryLayerProfile(aClass)};
    }

    private String returnRepositoryLayerProfile(Class<?> aClass) {
        if (aClass.getSimpleName().contains("DataJPATest")) {
            return Profiles.DATAJPA;
        } else if (aClass.getSimpleName().contains("JPATest")) {
            return Profiles.JPA;
        } else if (aClass.getSimpleName().contains("JDBCTest")) {
            return Profiles.JDBC;
        }
        return "error";
    }
}