package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoWebShopTests {

    String authCookieName = "NOPCOMMERCE.AUTH",
            email = "vbdv@feferf.ru",
            password = "itLf7@U@Bf6khGH";

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        RestAssured.baseURI = "https://demowebshop.tricentis.com";

//        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @Test
    void addToCartTest() {
        /*
        curl 'https://demowebshop.tricentis.com/addproducttocart/details/72/1' \
        -H 'Accept: *' \
        -H 'Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7' \
        -H 'Connection: keep-alive' \
        -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
        -H 'Cookie: Nop.customer=de725134-0135-418f-8c0a-0a72f67796cd; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72; ARRAffinity=754c43f3fb666b4689fd8452291c08520e941e1737ad17b31babd87059cc27ae; ARRAffinitySameSite=754c43f3fb666b4689fd8452291c08520e941e1737ad17b31babd87059cc27ae; __atuvc=1%7C43; __atuvs=6355238c3c4d1ac0000; __utma=78382081.389300604.1666524045.1666524045.1666524045.1; __utmc=78382081; __utmz=78382081.1666524045.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmt=1; __utmb=78382081.1.10.1666524045 \
        -H 'Origin: https://demowebshop.tricentis.com' \
        -H 'Referer: https://demowebshop.tricentis.com/build-your-cheap-own-computer' \
        -H 'Sec-Fetch-Dest: empty' \
        -H 'Sec-Fetch-Mode: cors' \
        -H 'Sec-Fetch-Site: same-origin' \
        -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36' \
        -H 'X-Requested-With: XMLHttpRequest' \
        -H 'sec-ch-ua: ".Not/A)Brand";v="99", "Google Chrome";v="103", "Chromium";v="103"' \
        -H 'sec-ch-ua-mobile: ?0' \
        -H 'sec-ch-ua-platform: "macOS"' \
        --data-raw 'product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1' \
        --compressed
         */

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer", "de725134-0135-418f-8c0a-0a72f67796cd; ")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));
    }

    @Test
    void addToOldCartAsAnonymTest() {
        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));
    }

    @Test
    void addToOldCartAsAuthorisedTest() {
        // NOPCOMMERCE.AUTH=63E5398085346F0233D4A2299C60E9E0710421161177AABC36623E720D917BB24A026CC8DCA04040109FCDCDD3081EF7F9262065FA5581E8F933B5B6F34EC6BFD264D5A4A92168BC5AF1EBF77F52470D22079504CB57DEE018A853E4D54F9C7EA98654CE3975D875710DB36A41A63226C38E81010357E456CAED7292634757B0F42E965F580DF2C930D899551C6B7F82; Nop.customer=bccc3d3b-96f1-4e02-aef5-52bd4ae25d47;

        String authCookie = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "vbdv@feferf.ru")
                .formParam("Password", "itLf7@U@Bf6khGH")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");

        String body = "product_attribute_72_5_18=52" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&product_attribute_72_8_30=93" +
                "&product_attribute_72_8_30=94" +
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", authCookie)
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));
    }

    @Test
    void addToOldCartAsAuthorisedSizeInWebTest() {

        String authCookieName = "NOPCOMMERCE.AUTH";
        String authCookieValue = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "vbdv@feferf.ru")
                .formParam("Password", "itLf7@U@Bf6khGH")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");

        String body = "product_attribute_72_5_18=52" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&product_attribute_72_8_30=93" +
                "&product_attribute_72_8_30=94" +
                "&addtocart_72.EnteredQuantity=1";

        String cartSize = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", authCookieValue)
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .extract()
                .path("updatetopcartsectionhtml");

        open("/Themes/DefaultClean/Content/images/logo.png");

        Cookie authCookie = new Cookie(authCookieName, authCookieValue);
        WebDriverRunner.getWebDriver().manage().addCookie(authCookie);

        open("");
        $(".cart-qty").shouldHave(Condition.text(cartSize));
    }
}
