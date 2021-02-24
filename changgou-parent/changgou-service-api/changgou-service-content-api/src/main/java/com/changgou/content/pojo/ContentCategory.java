package com.changgou.content.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:ContentCategory构建
 *
 *****/
@ApiModel(description = "ContentCategory",value = "ContentCategory")
@Table(name="tb_content_category")
@Data
public class ContentCategory implements Serializable{

	@ApiModelProperty(value = "类目ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//类目ID

	@ApiModelProperty(value = "分类名称",required = false)
    @Column(name = "name")
	private String name;//分类名称




}
