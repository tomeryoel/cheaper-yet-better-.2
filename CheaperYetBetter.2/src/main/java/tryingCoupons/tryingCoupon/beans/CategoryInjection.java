package tryingCoupons.tryingCoupon.beans;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tryingCoupons.tryingCoupon.repositories.CategoryRepo;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class CategoryInjection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(value = EnumType.STRING)
    private Category category;

    public CategoryInjection(Category category){
        id = category.value;
        this.category=category;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private List<Coupon> couponsBelongs= new ArrayList<>();


    @Override
    public String toString() {
        return "CategoryFill{" +
                "id=" + id +
                ", category=" + getCategory()
                 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoryInjection that = (CategoryInjection) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


@Configuration
@RequiredArgsConstructor
class Injection{
    private final CategoryRepo categoryJPA;

    @Bean
    protected void categoryInjection(){
        Arrays.stream(Category.values()).forEach(c-> categoryJPA.save(CategoryInjection.builder()
                        .category(c)
                .build()));
    }

}




