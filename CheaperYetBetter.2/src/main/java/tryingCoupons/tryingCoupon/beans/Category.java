package tryingCoupons.tryingCoupon.beans;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    CARS,
    PAINTING,
    WHEELS,
    YACHT,
    MOTORCYCLES;

    public final int value = 1 + ordinal();


    /**
     *
     * @param categoryValue - category by number
     * @return - an enum of company
     */
    public static Category getCategory(int categoryValue){
        List<Category> categoryList;
        categoryList = Arrays.stream(Category.values()).filter(c -> c.value == categoryValue).limit(1).collect(Collectors.toList());
        return categoryList.get(0);
    }
}
