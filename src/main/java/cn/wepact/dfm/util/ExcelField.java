package cn.wepact.dfm.util;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.*;

/**
 * 列属性信息
 * <p>
 * 支持Java对象数据类型：Boolean、String、Short、Integer、Long、Float、Double、Date
 * 支持Excel的Cell类型为：String
 *
 * @author xuxueli 2017-09-08 20:22:41
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelField {

    /**
     * 列名称
     *
     * @return String
     */
    String name() default "";

    /**
     * 列宽 (大于0时生效; 如果不指定列宽，将会自适应调整宽度；)
     *
     * @return int
     */
    int width() default 0;

    /**
     * 水平对齐方式
     *
     * @return HorizontalAlignment
     */
    HorizontalAlignment align() default HorizontalAlignment.LEFT;

    /**
     * 时间格式化，日期类型时生效
     *
     * @return String
     */
    String dateformat() default "yyyy/M/d";

    /**
     * 第几行
     *
     * @return int
     */
    int column() default -1;

    /**
     * 是否是判断数据的状态位
     */
    boolean checkDataFlag() default false;
}
