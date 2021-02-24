package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Xu Rui
 * @date 2021/2/22 22:48
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Resource
    private SkuFeign skuFeign;

    @Resource
    private SkuEsMapper skuEsMapper;

    /**
     * 可以实现索引库的增删改查[高级搜索]
     */
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /***
     * 条件搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //搜索条件封装
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildBasicQuery(searchMap);

        //集合搜索
        Map<String, Object> resultMap = searchList(nativeSearchQueryBuilder);

//        //分类分组查询实现
//        //如果用户选择了分类，并将分类作为搜索条件，则不需要对分类进行分组搜索
//        if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))){
//            List<String> categoryList = searchCategoryList(nativeSearchQueryBuilder);
//            resultMap.put("categoryList", categoryList);
//        }
//
//
//        //查询品牌集合[搜索条件]
//        if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))){
//            List<String> brandList = searchBrandList(nativeSearchQueryBuilder);
//            resultMap.put("brandList", brandList);
//        }
//
//        //规格查询
//        Map<String, Set<String>> specList = searchSpecList(nativeSearchQueryBuilder);
//        resultMap.put("specList", specList);

        //分组搜索实现
        Map<String, Object> groupMap = searchGroupList(nativeSearchQueryBuilder, searchMap);
        resultMap.putAll(groupMap);

        return resultMap;
    }

    /**
     * 分组查询->分类分组、品牌分组、规格分组
     * @param nativeSearchQueryBuilder
     * @return
     */
    private Map<String, Object> searchGroupList(NativeSearchQueryBuilder nativeSearchQueryBuilder, Map<String, String> searchMap) {
        /**
         * 分组查询
         * addAggregation添加聚合操作
         * 参数1.取别名
         * 参数2.根据哪个域进行分组查询
         */
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        }
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        }
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));

        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        Map<String, Object> groupMapResult = new HashMap<>();

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations()：获取的是集合，可以根据多个域进行分组
         * get("skuCategory")：获取指定域的集合数
         */
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("category"))) {
            StringTerms categoryTerms = aggregatedPage.getAggregations().get("skuCategory");

            //获取分类分组集合数据
            List<String> categoryList = getGroupList(categoryTerms);
            groupMapResult.put("categoryList", categoryList);
        }
        if(searchMap == null || StringUtils.isEmpty(searchMap.get("brand"))) {
            StringTerms brandTerms = aggregatedPage.getAggregations().get("skuBrand");
            //获取品牌分组集合数据
            List<String> brandList = getGroupList(brandTerms);
            groupMapResult.put("brandList", brandList);
        }
        StringTerms specTerms = aggregatedPage.getAggregations().get("skuSpec");
        //获取规格分组集合数据
        List<String> specList = getGroupList(specTerms);
        Map<String, Set<String>> specMap = putAllSpec(specList);
//        groupMapResult.put("specList", specMap);
        groupMapResult.put("specMap", specMap);

        return groupMapResult;
    }

    /**
     * 获取分组集合数据
     * @param stringTerms
     * @return
     */
    private List<String> getGroupList(StringTerms stringTerms) {
        List<String> groupList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String fieldName = bucket.getKeyAsString();//其中的一个分类名字
            groupList.add(fieldName);
        }
        return groupList;
    }


    /**
     * 搜索条件封装
     * @param searchMap
     * @return
     */
    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {
        //NativeSearchQueryBuilder：搜索条件构建对象，用于封装各种搜索条件
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //构建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //分页，用户如果不传分页参数，则默认第1页
        Integer pageNum = 1;   //默认第一页
        Integer size = 10;   //默认查询的数据条数

        if(searchMap != null && searchMap.size() > 0){
            //根据关键词搜索
            String keywords = searchMap.get("keywords");
            if (!StringUtils.isEmpty(searchMap.get("keywords")))
                boolQueryBuilder.must(QueryBuilders.matchQuery("name", keywords));

            //输入了分类
            String category = searchMap.get("category");
            if (!StringUtils.isEmpty(category))
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", category));

            //输入了品牌
            String brand = searchMap.get("brand");
            if (!StringUtils.isEmpty(brand))
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", brand));

            /**
             * 规格过滤实现
             * 要求 参数为 spec_网络制式：电信4G、spec_显示屏尺寸：4.0-4.9英寸
             */
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                //如果key以spec_开始，则表示规格筛选查询
                if(key.startsWith("spec_")){
                    String value = entry.getValue();
                    //spec_网络
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }

            /**
             * 价格过滤 0-500元 500-1000元 3000元以上
             * 去掉元，以上
             * 根据"-"分割 [0, 500] [500, 1000] [3000]
             */
            String price = searchMap.get("price");
            if (!StringUtils.isEmpty(price)){
                price = price.replace("元", "").replace("以上", "");
                String[] prices = price.split("-");
                if(prices.length > 0){
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                    if(prices.length == 2)
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])).lte(prices[1]));
                }
            }

            //分页
            String num = searchMap.get("pageNum");
            if(!StringUtils.isEmpty(num)){
                int n = Integer.parseInt(num);
                pageNum = Math.max(n, 1);
            }

            //排序实现
            String sortField = searchMap.get("sortField");
            String sortRule = searchMap.get("sortRule");
            if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)){
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField)   //排序域
                        .order(SortOrder.valueOf(sortRule)));   //排序规则
            }
        }

        //分页，用户如果不传分页参数，则默认第1页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum - 1, size));

        //将boolQueryBuilder填充给nativeSearchQueryBuilder
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 结果集搜索
     * @param nativeSearchQueryBuilder
     * @return
     */
    private Map<String, Object> searchList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {

        //添加高亮
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");   //指定高亮域
        //前缀    后缀
        field.preTags("<em style=\"color:red\">").postTags("</em>");
        //碎片长度  关键词数据的长度
        field.fragmentSize(100);

        nativeSearchQueryBuilder.withHighlightFields(field);

        /**
         * 执行搜索，响应结果
         * 参数1.搜索条件封装
         * 参数2.搜索的结果集需要转换的类型
         * 返回结果搜索结果集的封装
         */
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(
                nativeSearchQueryBuilder.build(),   //搜索条件封装
                SkuInfo.class,                      //数据集合要转换的类型的字节码
                new SearchResultMapperImpl()         //执行搜索后，将数据结果集封装到该对象中
        );

        //分页参数-总记录数
        long totalElements = page.getTotalElements();

        //总页数
        int totalPages = page.getTotalPages();

        //获取数据结果集
        List<SkuInfo> contents = page.getContent();

        //封装一个Map存储所有数据，返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", contents);
        resultMap.put("total", totalElements);
        resultMap.put("totalPages", totalPages);

        //获取搜索封装信息 分页数据
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        Pageable pageable = query.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        resultMap.put("pageSize", pageSize);
        resultMap.put("pageNum", pageNumber);

        return resultMap;
    }

    /**
     * 分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    private List<String> searchCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        /**
         * 分组查询
         * addAggregation添加聚合操作
         * 参数1.取别名
         * 参数2.根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations()：获取的是集合，可以根据多个域进行分组
         * get("skuCategory")：获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuCategory");

        List<String> categoryList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String categoryName = bucket.getKeyAsString();//其中的一个分类名字
            categoryList.add(categoryName);
        }
        return categoryList;
    }

    /**
     * 品牌分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    private List<String> searchBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        /**
         * 分组查询
         * addAggregation添加聚合操作
         * 参数1.取别名
         * 参数2.根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations()：获取的是集合，可以根据多个域进行分组
         * get("skuCategory")：获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuBrand");

        List<String> brandList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String brandName = bucket.getKeyAsString();//其中的一个品牌名字
            brandList.add(brandName);
        }
        return brandList;
    }

    /**
     * 规格分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    private Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        /**
         * 分组查询
         * addAggregation添加聚合操作
         * 参数1.取别名
         * 参数2.根据哪个域进行分组查询
         */
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(10000));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取分组数据
         * aggregatedPage.getAggregations()：获取的是集合，可以根据多个域进行分组
         * get("skuSpec")：获取指定域的集合数
         */
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuSpec");

        List<String> specList = new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String specName = bucket.getKeyAsString();//其中的一个品牌名字
            specList.add(specName);
        }

        //规格汇总合并
        Map<String, Set<String>> allSpec = putAllSpec(specList);
        return allSpec;
    }

    /**
     * 规格汇总合并
     * @param specList
     * @return
     */
    private Map<String, Set<String>> putAllSpec(List<String> specList) {
        /**
         * [{"手机屏幕尺寸":"5寸","网络":"联通2G","颜色":"黑","测试":"实施","机身内存":"16G","存储":"16G","像素":"800万像素"},
         * {"手机屏幕尺寸":"5寸","网络":"联通2G","颜色":"黑","测试":"实施","机身内存":"16G","存储":"16G","像素":"800万像素"}]
         *
         * 1.获取所有规格数据
         * 2.将所有规格数据转换成Map
         * 3.定义一个Map<String,Set>,key是规格名字，防止重复所以用Map，valu是规格值，规格值有多个，所以用集合，为了防止规格重复，用Set去除重复
         * 4.循环规格的Map，将数据填充到定义的Map<String,Set>中
         */
        Map<String, Set<String>> allSpec = new HashMap<>();
        for (String spec : specList) {
            Map<String, String> specMap = JSON.parseObject(spec, new TypeReference<Map<String, String>>(){});
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                String key = entry.getKey();    //规格名字
                String value = entry.getValue();    //规格值

                Set<String> specSet = allSpec.get(key);
                if(specSet == null){
                    specSet = new HashSet<>();
                }
                specSet.add(value);
                allSpec.put(key, specSet);
            }
        }
        return allSpec;
    }

    /**
     * 导入索引库
     */
    @Override
    public void importSku() {
        //Feign调用，查询List<Sku>
        Result<List<Sku>> skuResult = skuFeign.findByStatus("1");

        //List<Sku>转成List<SkuInfo>
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuResult.getData()), SkuInfo.class);

        //对于每一个SkuInfo
        for (SkuInfo skuInfo : skuInfoList) {
            //把spec转换为Map
            Map<String, Object> map = JSON.parseObject(skuInfo.getSpec(), new TypeReference<Map<String, Object>>(){});
            //如果需要生成动态域，只需要将该域存储到一个Map<String, Object>对象中即可
            skuInfo.setSpecMap(map);
        }

        //调用Dao实现数据批量导入
        skuEsMapper.saveAll(skuInfoList);
    }
}
