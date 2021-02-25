package com.changgou.order.pojo;

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
 * @Description:CategoryReport构建
 *
 *****/
@ApiModel(description = "CategoryReport",value = "CategoryReport")
@Table(name="tb_category_report")
@Data
public class CategoryReport implements Serializable{

	@ApiModelProperty(value = "1级分类",required = false)
    @Column(name = "category_id1")
	private Integer categoryId1;//1级分类

	@ApiModelProperty(value = "2级分类",required = false)
    @Column(name = "category_id2")
	private Integer categoryId2;//2级分类

	@ApiModelProperty(value = "3级分类",required = false)
    @Column(name = "category_id3")
	private Integer categoryId3;//3级分类

	@ApiModelProperty(value = "统计日期",required = false)
	@Id
    @Column(name = "count_date")
	private Date countDate;//统计日期

	@ApiModelProperty(value = "销售数量",required = false)
    @Column(name = "num")
	private Integer num;//销售数量

	@ApiModelProperty(value = "销售额",required = false)
    @Column(name = "money")
	private Integer money;//销售额




}
