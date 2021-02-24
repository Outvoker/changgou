package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/****
 * @author Xu Rui
 * @Description:UndoLog构建
 *
 *****/
@ApiModel(description = "UndoLog",value = "UndoLog")
@Table(name="undo_log")
@Data
public class UndoLog implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "branch_id")
	private Long branchId;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "xid")
	private String xid;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "rollback_info")
	private String rollbackInfo;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_status")
	private Integer logStatus;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_created")
	private Date logCreated;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "log_modified")
	private Date logModified;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "ext")
	private String ext;//




}
