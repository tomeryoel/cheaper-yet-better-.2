package tryingCoupons.tryingCoupon.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private Integer coupon_id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn( referencedColumnName = "id")
    private Company company_id_sql;

    @Enumerated(value = EnumType.ORDINAL)

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryInjection category;
    @Column(name = "title",nullable = false,length = 40)
    private String title;
    @Column(name = "description",nullable = false,length = 40)
    private String description;
    @Column(name = "start_date",nullable = false,length = 40)
    private Date start_date;
    @Column(name = "end_date",nullable = false,length = 40)
    private Date end_date;
    @Column(name = "amount",nullable = false,length = 40)
    private int amount;
    @Column(name = "price",nullable = false,length = 40)
    private double price;
    @Column(name = "image",nullable = false,length = 40)
    private String image;
    @Column(name = "category_by_id")
    private int category_id_bynum;
    @Column(name = "company_id")
    private int companyId;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "customer_vs_coupons",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    @Singular
    private Set<Customer> customers = new HashSet<>();



    @Override
    public String toString() {
        return "coupons{" +
                "coupon_id=" + coupon_id +
                ", category=" + getCategory() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", start_date=" + getStart_date() +
                ", end_date=" + getEnd_date() +
                ", amount=" + getAmount() +
                ", price=" + getPrice() +
                ", image='" + getImage() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Coupon coupon = (Coupon) o;
        return coupon_id != null && Objects.equals(coupon_id, coupon.coupon_id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Company getCompany_id_sql() {
        return company_id_sql;
    }

    public void setCompany_id_sql(Company company_id_sql) {
        this.company_id_sql = company_id_sql;
    }

    public CategoryInjection getCategory() {
        return category;
    }

    public void setCategory(CategoryInjection category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategory_id_bynum() {
        return category_id_bynum;
    }

    public void setCategory_id_bynum(int category_id_bynum) {
        this.category_id_bynum = category_id_bynum;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
