package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Category构建
 * @date 2021/2/19 21:28
 *****/
@ApiModel(description = "Category",value = "Category")
@Table(name="tb_category")
@Data
public class Category implements Serializable{

	@ApiModelProperty(value = "分类ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//分类ID
	@ApiModelProperty(value = "分类名称",required = false)
    @Column(name = "name")
	private String name;//分类名称
	@ApiModelProperty(value = "商品数量",required = false)
    @Column(name = "goods_num")
	private Integer goodsNum;//商品数量
	@ApiModelProperty(value = "是否显示",required = false)
    @Column(name = "is_show")
	private String isShow;//是否显示
	@ApiModelProperty(value = "是否导航",required = false)
    @Column(name = "is_menu")
	private String isMenu;//是否导航
	@ApiModelProperty(value = "排序",required = false)
    @Column(name = "seq")
	private Integer seq;//排序
	@ApiModelProperty(value = "上级ID",required = false)
    @Column(name = "parent_id")
	private Integer parentId;//上级ID
	@ApiModelProperty(value = "模板ID",required = false)
    @Column(name = "template_id")
	private Integer templateId;//模板ID

}
