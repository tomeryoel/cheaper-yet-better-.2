package tryingCoupons.tryingCoupon.beans;

import lombok.Builder;


@Builder
public class UserProp {
    private String username;
    private String password;


    public UserProp(String username , String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserProp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
