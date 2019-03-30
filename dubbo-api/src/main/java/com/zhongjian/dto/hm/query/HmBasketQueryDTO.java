package com.zhongjian.dto.hm.query;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: ldd
 */
@Data
public class HmBasketQueryDTO implements Serializable {

    private static final long serialVersionUID = 197018972999527001L;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 用户登入token
     */
    private String loginToken;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;
}
