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
 * @Description:Cities构建
 *
 *****/
@ApiModel(description = "Cities",value = "Cities")
@Table(name="tb_cities")
@Data
public class Cities implements Serializable{

	@ApiModelProperty(value = "城市ID",required = false)
	@Id
    @Column(name = "cityid")
	private String cityid;//城市ID

	@ApiModelProperty(value = "城市名称",required = false)
    @Column(name = "city")
	private String city;//城市名称

	@ApiModelProperty(value = "省份ID",required = false)
    @Column(name = "provinceid")
	private String provinceid;//省份ID




}
