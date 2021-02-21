package ${package_pojo};
<#if swagger==true>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
</#if>
import javax.persistence.*;
import java.io.Serializable;
<#list typeSet as set>
import ${set};
</#list>
/****
 * @author Xu Rui
 * @Description:${Table}构建
 *
 *****/
<#if swagger==true>
@ApiModel(description = "${Table}",value = "${Table}")
</#if>
@Table(name="${TableName}")
@Data
public class ${Table} implements Serializable{

<#list models as model>
	<#if swagger==true>
	@ApiModelProperty(value = "${model.desc!""}",required = false)
	</#if>
	<#if model.id==true>
	@Id
	<#if model.identity=='YES'>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	</#if>
	</#if>
    @Column(name = "${model.column}")
	private ${model.simpleType} ${model.name};//${model.desc!""}

</#list>



}
