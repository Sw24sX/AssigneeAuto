package com.example.assigneeauto.config.key.generator;

import com.example.assigneeauto.service.WeightByNotValuesApi;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class PercentWeightKeyGenerator implements KeyGenerator {
    private static final String DELIMITER = "_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder builder = new StringBuilder();
        for(Object param : params) {
            if (param instanceof WeightByNotValuesApi) {
                builder.append(((WeightByNotValuesApi) param).getCacheKey());
            } else {
                builder.append(param.hashCode());
            }
            builder.append(DELIMITER);
        }

        return builder.toString();
    }
}
