/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package calculator.common;

import calculator.engine.ObjectMapper;
import calculator.engine.annotation.Internal;
import graphql.Assert;
import graphql.execution.ResultPath;
import graphql.language.Argument;
import graphql.language.ArrayValue;
import graphql.language.BooleanValue;
import graphql.language.Directive;
import graphql.language.EnumValue;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.NamedNode;
import graphql.language.StringValue;
import graphql.language.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static calculator.common.GraphQLUtil.PATH_SEPARATOR;


@Internal
public class CommonUtil {

    /**
     * Get the dependency source name list.
     *
     * @param directive the directive which may use dependency source
     * @return the dependency source name list
     */
    public static List<String> getDependenceSourceFromDirective(Directive directive) {
        Object dependencySources = getArgumentFromDirective(directive, "dependencySources");
        if (dependencySources instanceof String) {
            return Collections.singletonList((String) dependencySources);
        }

        return (List<String>) dependencySources;
    }


    /**
     * Get the dependency source name list.
     *
     * @param value the argument value
     * @return the dependency source name list
     */
    public static List<String> getDependencySources(Value value) {
        if (value instanceof StringValue) {
            return Collections.singletonList(((StringValue) value).getValue());
        }

        if (value instanceof ArrayValue) {
            List<String> dependencySources = new ArrayList<>();
            for (Value element : ((ArrayValue) value).getValues()) {
                if (element instanceof StringValue) {
                    dependencySources.add(((StringValue) element).getValue());
                } else {
                    throw new RuntimeException("error value type: " + element.getClass().getSimpleName());
                }
            }
            return dependencySources;
        }

        throw new RuntimeException("error value type: " + value.getClass().getSimpleName());
    }

    /**
     * Get argument value on directive by argument name.
     *
     * @param directive    dir
     * @param argumentName argument name
     * @param <T>          the type of argument value
     * @return the argument value
     */
    public static <T> T getArgumentFromDirective(Directive directive, String argumentName) {
        Argument argument = directive.getArgument(argumentName);
        if (argument == null) {
            return null;
        }

        return (T) parseValue(argument.getValue());
    }


    public static Object parseValue(Value value) {
        if (value instanceof StringValue) {
            return ((StringValue) value).getValue();
        }

        if (value instanceof BooleanValue) {
            return ((BooleanValue) value).isValue();
        }

        if (value instanceof IntValue) {
            return ((IntValue) value).getValue();
        }

        if (value instanceof FloatValue) {
            return ((FloatValue) value).getValue();
        }

        if (value instanceof EnumValue) {
            return ((EnumValue) value).getName();
        }

        if (value instanceof ArrayValue) {
            List<Object> listValue = new ArrayList<>();
            for (Value element : ((ArrayValue) value).getValues()) {
                Object elementValue = parseValue(element);
                listValue.add(elementValue);
            }
            return listValue;
        }

        throw new RuntimeException("can not invoke here.");
    }


    public static boolean isValidEleName(String name) {
        try {
            Assert.assertValidName(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // 获取当前字段的查询路径，使用 '.' 分割
    public static String fieldPath(final ResultPath stepInfo) {
        StringBuilder sb = new StringBuilder();
        ResultPath tmpEnv = stepInfo;
        while (tmpEnv != null) {
            if (!tmpEnv.isNamedSegment()) {
                tmpEnv = tmpEnv.getParent();
                continue;
            }

            String segmentName = tmpEnv.getSegmentName();
            if (segmentName == null || segmentName.length() == 0) {
                tmpEnv = tmpEnv.getParent();
                continue;
            }

            if (sb.length() == 0) {
                sb.append(segmentName);
            } else {
                sb.insert(0, segmentName + PATH_SEPARATOR);
            }
            tmpEnv = tmpEnv.getParent();
        }

        return sb.toString();
    }


    private static final Set<Class<?>> WRAPPER_TYPES = createWrapperTypes();

    private static Set<Class<?>> createWrapperTypes() {
        Set<Class<?>> wrapperTypes = new LinkedHashSet<>(8);
        wrapperTypes.add(Boolean.class);
        wrapperTypes.add(Byte.class);
        wrapperTypes.add(Character.class);
        wrapperTypes.add(Short.class);
        wrapperTypes.add(Integer.class);
        wrapperTypes.add(Long.class);
        wrapperTypes.add(Float.class);
        wrapperTypes.add(Double.class);
        return Collections.unmodifiableSet(wrapperTypes);
    }

    private static final Set<Class<?>> MATH_TYPES = createMathTypes();

    private static Set<Class<?>> createMathTypes() {
        Set<Class<?>> wrapperTypes = new LinkedHashSet<>(2);
        wrapperTypes.add(BigDecimal.class);
        wrapperTypes.add(BigInteger.class);
        return Collections.unmodifiableSet(wrapperTypes);
    }

    /**
     * Determines if the class of specified object represents a math type.
     */
    public static boolean isMathType(Object object) {
        return MATH_TYPES.contains(object.getClass());
    }


    /**
     * Determines if the class of specified object represents a wrapper type.
     */
    public static boolean isWrapperType(Object object) {
        return WRAPPER_TYPES.contains(object.getClass());
    }

    /**
     * Determines if the class of specified object represents a basic type: primitive type, wrapper type or CharSequence.
     */
    public static boolean isBasicType(Object object) {
        return object.getClass().isPrimitive() || isWrapperType((object)) || isMathType(object) || object instanceof CharSequence;
    }

    /**
     * Return the first NamedNode which match the provided name.
     */
    public static <T extends NamedNode<T>> T findNodeByName(List<T> namedNodes, String name) {
        for (T namedNode : namedNodes) {
            if (Objects.equals(namedNode.getName(), name)) {
                return namedNode;
            }
        }
        return null;
    }

    /**
     * Convert object to script arguments.
     *
     * @param objectMapper objectMapper which used to convert object to script argument
     * @param object       object
     * @return script argument
     */
    public static Object getScriptEnv(ObjectMapper objectMapper, Object object) {
        if (object == null) {
            return null;
        }

        if (CommonUtil.isBasicType(object)) {
            return Collections.singletonMap("ele", object);
        } else {
            return objectMapper.toSimpleCollection(object);
        }
    }

}
