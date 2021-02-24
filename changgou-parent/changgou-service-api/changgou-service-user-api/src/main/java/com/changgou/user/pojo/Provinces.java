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
 * @Description:Provinces构建
 *
 *****/
@ApiModel(description = "Provinces",value = "Provinces")
@Table(name="tb_provinces")
@Data
public class Provinces implements Serializable{

	@ApiModelProperty(value = "省份ID",required = false)
	@Id
    @Column(name = "provinceid")
	private String provinceid;//省份ID

	@ApiModelProperty(value = "省份名称",required = false)
    @Column(name = "province")
	private String province;//省份名称




}
