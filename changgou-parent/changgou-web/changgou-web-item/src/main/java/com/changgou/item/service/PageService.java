package com.changgou.item.service;

/**
 * 描述
 *
 * @author Xu Rui
 * @version 1.0
 * @package com.changgou.item.service *
 * @since 1.0
 */
public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     * @param id
     */
    void createPageHtml(Long id);
}
