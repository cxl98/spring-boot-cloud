package com.cxl.cloud.common.util.constant;

/**
 * 系统级静态变量
 */
public class SystemConstant {
    /**
     * 超级管理员ID
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * 数据标识
     */
    public static final String DATA_ROWS = "rows";

    /**
     * 真
     */
    public static final String TRUE = "true";
    /**
     * 假
     */
    public static final String FALSE = "false";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private final int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 通用字典
     */
    public enum MacroType {

        /**
         * 类型
         */
        TYPE(0),

        /**
         * 参数
         */
        PARAM(1);

        private final int value;

        MacroType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     * 通用变量，表示可用、禁用、显示、隐藏、是、否
     */
    public enum StatusType {

        /**
         * 禁用，隐藏
         */
        DISABLE(0),

        /**
         * 可用，显示
         */
        ENABLE(1),

        /**
         * 显示
         */
        SHOW(1),

        /**
         * 隐藏
         */
        HIDDEN(0),

        /**
         * 是
         */
        YES(1),

        /**
         * 否
         */
        NO(0);

        private final int value;

        StatusType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


}
