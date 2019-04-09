package com.zhongjian.dto.hm.basket.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ldd
 */
@Data
public class HmBasketEditQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 用户登入token
     */
    private Integer uid;

    /**
     * 数量
     */
    private String amount;

    /**
     * 备注
     */
    private String remark;
}
