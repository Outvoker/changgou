package com.changgou.content.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Content构建
 *
 *****/
@ApiModel(description = "Content",value = "Content")
@Table(name="tb_content")
@Data
public class Content implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//

	@ApiModelProperty(value = "内容类目ID",required = false)
    @Column(name = "category_id")
	private Long categoryId;//内容类目ID

	@ApiModelProperty(value = "内容标题",required = false)
    @Column(name = "title")
	private String title;//内容标题

	@ApiModelProperty(value = "链接",required = false)
    @Column(name = "url")
	private String url;//链接

	@ApiModelProperty(value = "图片绝对路径",required = false)
    @Column(name = "pic")
	private String pic;//图片绝对路径

	@ApiModelProperty(value = "状态,0无效，1有效",required = false)
    @Column(name = "status")
	private String status;//状态,0无效，1有效

	@ApiModelProperty(value = "排序",required = false)
    @Column(name = "sort_order")
	private Integer sortOrder;//排序




}
