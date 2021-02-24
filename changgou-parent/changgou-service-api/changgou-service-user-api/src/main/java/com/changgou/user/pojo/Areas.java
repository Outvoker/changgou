package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @author Xu Rui
 * @Description:Areas构建
 *
 *****/
@ApiModel(description = "Areas",value = "Areas")
@Table(name="tb_areas")
@Data
public class Areas implements Serializable{

	@ApiModelProperty(value = "区域ID",required = false)
	@Id
    @Column(name = "areaid")
	private String areaid;//区域ID

	@ApiModelProperty(value = "区域名称",required = false)
    @Column(name = "area")
	private String area;//区域名称

	@ApiModelProperty(value = "城市ID",required = false)
    @Column(name = "cityid")
	private String cityid;//城市ID




}
