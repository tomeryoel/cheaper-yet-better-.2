package tryingCoupons.tryingCoupon.threads;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tryingCoupons.tryingCoupon.repositories.CouponRepo;

import java.sql.Date;
import java.time.LocalDate;

@Component
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class DailyJob {
    private final CouponRepo couponRepo;


    @Async
    @Scheduled(fixedRate = 1000*60*60*24)
    public void cleaner(){
    couponRepo.deleteCouponsByDate(Date.valueOf(LocalDate.now().plusDays(1)));
    }
}
