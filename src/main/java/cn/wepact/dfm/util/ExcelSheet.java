package cn.wepact.dfm.util;

import java.lang.annotation.*;

/**
 * 表信息
 *
 * @author xuxueli 2017-09-08 20:51:26
 * @author wangbin 2019-08-07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelSheet {

    /**
     * 表名称
     *
     * @return String
     */
    String name() default "";

    /**
     * sheet页位置
     *
     * @return 索引
     */
    int index() default -1;

    /**
     * 表头占几行
     */
    int headRowCount() default -1;

    /**
     * 错误信息开始列数
     */
    int errorCellCount() default -1;
}

