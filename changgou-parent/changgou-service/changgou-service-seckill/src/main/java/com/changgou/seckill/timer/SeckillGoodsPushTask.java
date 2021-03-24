package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Xu Rui
 * @date 2021/3/19 13:12
 * 定时将秒杀商品存入到Redis缓存
 */
@Component
@Slf4j
public class SeckillGoodsPushTask {

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    private RedisTemplate redisTemplate;

    /****
     * 每30秒执行一次
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void loadGoodsPushRedis(){
        //获取时间段集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        //循环时间段
        for (Date dateMenu : dateMenus) {
            // namespace = SeckillGoods_20195712
            String extName = DateUtil.data2str(dateMenu,DateUtil.PATTERN_YYYYMMDDHH);

            //根据时间段数据查询对应的秒杀商品数据
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            // 1)商品必须审核通过  status=1
            criteria.andEqualTo("status","1");
            // 2)库存>0
            criteria.andGreaterThan("stockCount",0);
            // 3)开始时间<=活动开始时间
            criteria.andGreaterThanOrEqualTo("startTime",dateMenu);
            // 4)活动结束时间<开始时间+2小时
            criteria.andLessThan("endTime", DateUtil.addDateHour(dateMenu,2));
            // 5)排除之前已经加载到Redis缓存中的商品数据
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + extName).keys();
            if(keys!=null && keys.size()>0){
                criteria.andNotIn("id",keys);
            }

            //查询数据
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            //将秒杀商品数据存入到Redis缓存
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps("SeckillGoods_"+extName).put(seckillGood.getId(),seckillGood);
                redisTemplate.expireAt("SeckillGoods_"+extName,DateUtil.addDateHour(dateMenu, 2));
                //将商品ID存入到数组中
                //商品数据队列存储,防止高并发超卖
                Long[] ids = pushIds(seckillGood.getStockCount(), seckillGood.getId());
                redisTemplate.boundListOps("SeckillGoodsCountList_"+seckillGood.getId()).leftPushAll(ids);
//自增计数器
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGood.getId(),seckillGood.getStockCount());
            }
            log.info("add{}","SeckillGoods_"+extName);
        }
    }

    /***
     * 将商品ID存入到数组中
     * @param len:长度
     * @param id :值
     * @return
     */
    public Long[] pushIds(int len,Long id){
        Long[] ids = new Long[len];
        for (int i = 0; i <ids.length ; i++) {
            ids[i]=id;
        }
        return ids;
    }
}