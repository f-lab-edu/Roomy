package com.cony.roomy.core.accommodation.querydsl;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

public class CustomMySQLFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME = "match_against";
    private static final String FUNCTION_PATTERN = "match (?1, ?2) against (?3 in boolean mode)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        BasicType<Double> resultType = functionContributions
                .getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE);

        functionContributions.getFunctionRegistry()
                .registerPattern(FUNCTION_NAME, FUNCTION_PATTERN, resultType);
    }
}
