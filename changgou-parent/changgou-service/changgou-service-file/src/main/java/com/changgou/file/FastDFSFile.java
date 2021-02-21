package com.changgou.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csource.common.NameValuePair;

import java.io.Serializable;

/**
 * @author Xu Rui
 * @date 2021/2/20 23:02
 * 封装文件上传信息
 *  时间
 *  Author
 *  type
 *  size
 *  附加信息
 *  后缀
 *  文件内容->文件的字节数组
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FastDFSFile implements Serializable {
    //文件名字
    private String name;
    //文件内容
    private byte[] content;
    //文件扩展名
    private String ext;
    //文件MD5摘要值
    private String md5;
    //文件创建作者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        this(name, content, ext, null, null);
    }

    /**
     * 返回文件上传时的附加信息
     * @return  meta_list
     */
    public NameValuePair[] getMetaList(){
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("name", name);
        meta_list[1] = new NameValuePair("md5", md5);
        meta_list[2] = new NameValuePair("author", author);
        return meta_list;
    }

}
