package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @author Xu Rui
 * @Description:StockBack构建
 *
 *****/
@ApiModel(description = "StockBack",value = "StockBack")
@Table(name="tb_stock_back")
@Data
public class StockBack implements Serializable{

	@ApiModelProperty(value = "订单id",required = false)
    @Column(name = "order_id")
	private String orderId;//订单id

	@ApiModelProperty(value = "SKU的id",required = false)
	@Id
    @Column(name = "sku_id")
	private String skuId;//SKU的id

	@ApiModelProperty(value = "回滚数量",required = false)
    @Column(name = "num")
	private Integer num;//回滚数量

	@ApiModelProperty(value = "回滚状态",required = false)
    @Column(name = "status")
	private String status;//回滚状态

	@ApiModelProperty(value = "创建时间",required = false)
    @Column(name = "create_time")
	private Date createTime;//创建时间

	@ApiModelProperty(value = "回滚时间",required = false)
    @Column(name = "back_time")
	private Date backTime;//回滚时间




}
