package com.example.foodordering;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.jwt.secret=MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=",
        "app.jwt.expiration-seconds=3600"
})
class FoodorderingApplicationTests {

    @Test
    void contextLoads() {
    }
}
