package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Spec构建
 * @date 2021/2/19 21:28
 *****/
@ApiModel(description = "Spec",value = "Spec")
@Table(name="tb_spec")
@Data
public class Spec implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID
	@ApiModelProperty(value = "名称",required = false)
    @Column(name = "name")
	private String name;//名称
	@ApiModelProperty(value = "规格选项",required = false)
    @Column(name = "options")
	private String options;//规格选项
	@ApiModelProperty(value = "排序",required = false)
    @Column(name = "seq")
	private Integer seq;//排序
	@ApiModelProperty(value = "模板ID",required = false)
    @Column(name = "template_id")
	private Integer templateId;//模板ID

}
