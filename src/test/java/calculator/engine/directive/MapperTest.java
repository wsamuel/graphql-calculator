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

package calculator.engine.directive;

public class MapperTest {
//
//    private final GraphQLSchema wrappedSchema;
//    private final GraphQL graphQL;
//    {
//        wrappedSchema = SchemaWrapper.wrap(ConfigImpl.newConfig().build(), getCalSchema());
//        graphQL = GraphQL.newGraphQL(wrappedSchema)
//                .instrumentation(newInstance(ConfigImpl.newConfig().build()))
//                .build();
//    }
//
//    /**
//     * 1. 依赖另外一个节点对结果进行简单的转换；
//     *
//     * 2. 依赖另外一个节点进行join计算；
//     */
//    @Test
//    public void mapTest() {
//        String query = "query {\n" +
//                "    userInfo(id:5){\n" +
//                "        email\n" +
//                "        netName: email @map(mapper:\"'netName:' + email\")\n" +
//                "    }\n" +
//                "}";
//
//        ParseAndValidateResult validateResult = Validator.validateQuery(query, wrappedSchema);
//        assert !validateResult.isFailure();
//
//        ExecutionResult mapResult = graphQL.execute(query);
//        assert mapResult != null;
//        assert mapResult.getErrors().isEmpty();
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.email"), "5dugk@foxmail.com");
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.netName"), "netName:5dugk@foxmail.com");
//    }
//
//    @Test
//    public void testNodeFunction() {
//        String query = "" +
//                "query {\n" +
//                "\n" +
//                "    coupon(id: 3) @node(name: \"coupon\")\n" +
//                "    {\n" +
//                "        base\n" +
//                "        price\n" +
//                "    }\n" +
//                "\n" +
//                "    itemWithoutFilter: itemList(ids: [1,2,3,4,5])" +
//                "   {\n" +
//                "        itemId\n" +
//                "        originalPrice: salePrice\n" +
//                "        couponPrice: salePrice @map(mapper: \"salePrice-coupon.price\", dependencyNode:\"coupon\")\n" +
//                "    }\n" +
//                "    itemList(ids: [1,2,3,4,5])" +
//                "    @filter(predicate:\"coupon.price < salePrice\",dependencyNode:\"coupon\")" +
//                "   {\n" +
//                "        itemId\n" +
//                "        originalPrice: salePrice\n" +
//                "        couponPrice: salePrice @map(mapper: \"salePrice-coupon.price\", dependencyNode:\"coupon\")\n" +
//                "    }\n" +
//                "}";
//
//        ParseAndValidateResult validateResult = Validator.validateQuery(query, wrappedSchema);
//        assert !validateResult.isFailure();
//        ExecutionInput input = ExecutionInput.newExecutionInput(query).variables(Collections.singletonMap("itemIds", Arrays.asList(1, 2, 3))).build();
//        ExecutionResult result = graphQL.execute(input);
//
//
//        Map<String, List<Map<String, Object>>> data = (Map<String, List<Map<String, Object>>>) result.getData();
//        assert data.get("itemList").size() == 4;
//        Map<Number, Map<String, Object>> itemInfoById = data.get("itemList").stream().collect(Collectors.toMap(
//                ele -> (Number) ele.get("itemId"), Function.identity()
//        ));
//
//        // 30元的红包，原价为 id*20，券后价是 (id*20-30)
//        assert Objects.equals(itemInfoById.get(2).get("couponPrice"), 10);
//        assert Objects.equals(itemInfoById.get(3).get("couponPrice"), 30);
//        assert Objects.equals(itemInfoById.get(4).get("couponPrice"), 50);
//        assert Objects.equals(itemInfoById.get(5).get("couponPrice"), 70);
//
//        assert data.get("itemWithoutFilter").size() == 5;
//        Map<Number, Map<String, Object>> filterItemInfoById = data.get("itemWithoutFilter").stream().collect(Collectors.toMap(
//                ele -> (Number) ele.get("itemId"), Function.identity()
//        ));
//        assert Objects.equals(filterItemInfoById.get(1).get("couponPrice"), -10);
//        assert Objects.equals(filterItemInfoById.get(2).get("couponPrice"), 10);
//        assert Objects.equals(filterItemInfoById.get(3).get("couponPrice"), 30);
//        assert Objects.equals(filterItemInfoById.get(4).get("couponPrice"), 50);
//        assert Objects.equals(filterItemInfoById.get(5).get("couponPrice"), 70);
//    }
//
////    @Test
//    public void mapperWithJoin() {
//        String query = "" +
//                "query{\n" +
//                "    userInfoList(ids:[1,2])\n" +
//                "    @node(name: \"itemIds\", mapper: \"favoriteItemId\")\n" +
//                "    {\n" +
//                "        userId\n" +
//                "        name\n" +
//                "        favoriteItemId\n" +
//                "        itemId: userId @map(mapper: \"seq.get(seq.get(itemList,favoriteItemId),'itemId')\",dependencyNode: \"itemList\")\n" +
//                "        itemName: name @map(mapper: \"seq.get(seq.get(itemList,favoriteItemId),'name')\",dependencyNode: \"itemList\")\n" +
//                "        itemPrice: userId @map(mapper: \"seq.get(seq.get(itemList,favoriteItemId),'salePrice')\",dependencyNode: \"itemList\")\n" +
//                "    }\n" +
//                "\n" +
//                "    itemList(ids:1)\n" +
//                "    @argumentTransform(argument: \"ids\",operateType: MAP, exp:\"itemIds\", dependencyNode: \"itemIds\")\n" +
//                "    @node(name: \"itemList\", mapper: \"toMap('itemId')\")\n" +
//                "    {\n" +
//                "        itemId\n" +
//                "        name\n" +
//                "        salePrice\n" +
//                "    }\n" +
//                "}";
//
//        ParseAndValidateResult validateResult = Validator.validateQuery(query, wrappedSchema);
//        assert !validateResult.isFailure();
//
//        ExecutionResult mapResult = graphQL.execute(query);
//        assert mapResult != null;
//        assert mapResult.getErrors().isEmpty();
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.email"), "5dugk@foxmail.com");
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.netName"), "netName:5dugk@foxmail.com");
//    }
//
//
//
//
//    @Test
//    public void mapTestxx() {
//        String query = "\n" +
//                "query{\n" +
//                "    itemList(ids: [1,2])\n" +
//                "    @node(name: \"itemInfo\")\n" +
//                "    {\n" +
//                "        itemId\n" +
//                "        couponList{\n" +
//                "            couponId  \n" +
//                "            base @map(mapper:\"base - 100\")\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
////
////        ParseAndValidateResult validateResult = Validator.validateQuery(query, wrappedSchema);
////        assert !validateResult.isFailure();
//
//        ExecutionResult mapResult = graphQL.execute(query);
//        assert mapResult != null;
//        assert mapResult.getErrors().isEmpty();
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.email"), "5dugk@foxmail.com");
//        assert Objects.equals(getFromNestedMap(mapResult.getData(), "userInfo.netName"), "netName:5dugk@foxmail.com");
//    }


}