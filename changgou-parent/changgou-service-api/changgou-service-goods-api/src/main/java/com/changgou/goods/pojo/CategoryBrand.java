package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:CategoryBrand构建
 * @date 2021/2/19 21:28
 *****/
@ApiModel(description = "CategoryBrand",value = "CategoryBrand")
@Table(name="tb_category_brand")
@Data
public class CategoryBrand implements Serializable{

	@ApiModelProperty(value = "分类ID",required = false)
	@Id
    @Column(name = "category_id")
	private Integer categoryId;//分类ID
	@ApiModelProperty(value = "品牌ID",required = false)
	@Id
	@Column(name = "brand_id")
	private Integer brandId;//品牌ID

}
