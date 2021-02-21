package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Album构建
 *
 *****/
@ApiModel(description = "Album",value = "Album")
@Table(name="tb_album")
@Data
public class Album implements Serializable{

	@ApiModelProperty(value = "编号",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//编号

	@ApiModelProperty(value = "相册名称",required = false)
    @Column(name = "title")
	private String title;//相册名称

	@ApiModelProperty(value = "相册封面",required = false)
    @Column(name = "image")
	private String image;//相册封面

	@ApiModelProperty(value = "图片列表",required = false)
    @Column(name = "image_items")
	private String imageItems;//图片列表




}
