package entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*****
 * @Author: Xu Rui
 * @Description: entity:MQ消息封装
 ****/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable{

    //执行的操作  1：增加，2：修改,3：删除
    private int code;

    //数据
    private Object content;

    //发送的routkey
    @JSONField(serialize = false)
    private String routekey;

    //交换机
    @JSONField(serialize = false)
    private String exechange;


    public Message(int code, Object content) {
        this.code = code;
        this.content = content;
    }

}
