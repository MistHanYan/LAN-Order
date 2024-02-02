package com.lanorder.lanorderserver.common.util.count;

public class DiscountRule {
    /**
     *满减（可重复）
     *  */
    public static double maxOutAll(double amount, double max, double out){
        return amount-((int)(amount/max)*out);
    }

    /**
     * 打折*/
    public static double discount(double amount, double discount){
        return amount*discount;
    }

    /**
     * 满减（不可重复）
     * */
    public static double maxOutOne(double amount, double max, double out){
        return amount>max?amount-out:amount;
    }
}
