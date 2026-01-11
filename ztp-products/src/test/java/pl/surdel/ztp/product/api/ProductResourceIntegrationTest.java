package pl.surdel.ztp.product.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.surdel.ztp.product.api.dto.ProductRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductResourceIntegrationTest {

    @Test
    void shouldCreateValidProduct() {
        ProductRequest request = new ProductRequest();
        request.name = "TV123";
        request.category = "ELECTRONICS";
        request.price = "1500";
        request.quantity = 10;

        Long id =
                given()
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post("/api/v1/products")
                        .then()
                        .statusCode(200)
                        .body("id", notNullValue())
                        .body("name", equalTo("TV123"))
                        .body("quantity", equalTo(10))
                        .extract()
                        .jsonPath().getLong("id");

        given()
                .when()
                .get("/api/v1/products/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("name", equalTo("TV123"))
                .body("quantity", equalTo(10));

        given()
                .when()
                .delete("/api/v1/products/{id}", id)
                .then()
                .statusCode(204);
    }

    @Test
    void shouldRejectProductWithDuplicateName() {
        // 1. pierwszy produkt – poprawny
        ProductRequest first = new ProductRequest();
        first.name = "LAPTOP1";
        first.category = "ELECTRONICS";
        first.price = "3000";
        first.quantity = 5;

        Long id =
                given()
                        .contentType(ContentType.JSON)
                        .body(first)
                        .when()
                        .post("/api/v1/products")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath().getLong("id");

        // 2. drugi produkt – ta sama nazwa
        ProductRequest second = new ProductRequest();
        second.name = "LAPTOP1";
        second.category = "ELECTRONICS";
        second.price = "3500";
        second.quantity = 3;

        given()
                .contentType(ContentType.JSON)
                .body(second)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(422)
                .body("errors", hasItem("Product name must be unique."));

        // 3. cleanup
        given()
                .when()
                .delete("/api/v1/products/{id}", id)
                .then()
                .statusCode(204);
    }


    @Test
    void shouldRejectProductWithInvalidNameFormat() {
        ProductRequest request = new ProductRequest();

        // za krótka nazwa
        request.name = "ab";
        request.category = "BOOKS";
        request.price = "20";
        request.quantity = 2;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(422)
                .body("errors", hasItem(
                        "Product name must be between 3 and 20 characters long and contain only alphanumeric characters and spaces."
                ));

        // znak specjalny
        request.name = "ab!";

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(422)
                .body("errors", hasItem(
                        "Product name must be between 3 and 20 characters long and contain only alphanumeric characters and spaces."
                ));
    }

    @Test
    void shouldRejectProductWithPriceOutOfRangeForCategory() {
        ProductRequest request = new ProductRequest();
        request.name = "BOOK_TOO_EXPENSIVE";
        request.category = "BOOKS";   // zakres 5–1000
        request.price = "2000";       // poza zakresem
        request.quantity = 3;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/products")
                .then()
                .statusCode(422)
                .body("errors", hasItem(
                        "Product price is out of the allowed range for its category or category doesn't exist."
                ));
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        // 1. Utworzenie produktu
        ProductRequest create = new ProductRequest();
        create.name = "SHIRT1";
        create.category = "CLOTHES";
        create.price = "100";
        create.quantity = 5;

        Long id =
                given()
                        .contentType(ContentType.JSON)
                        .body(create)
                        .when()
                        .post("/api/v1/products")
                        .then()
                        .statusCode(200)
                        .body("id", notNullValue())
                        .body("name", equalTo("SHIRT1"))
                        .body("quantity", equalTo(5))
                        .extract()
                        .jsonPath().getLong("id");

        // 2. Aktualizacja produktu
        ProductRequest update = new ProductRequest();
        update.name = "SHIRT1";
        update.category = "CLOTHES";
        update.price = "150";
        update.quantity = 10;

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .put("/api/v1/products/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("name", equalTo("SHIRT1"))
                .body("price", equalTo(150))
                .body("quantity", equalTo(10));

        // 3. GET po update
        given()
                .when()
                .get("/api/v1/products/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("name", equalTo("SHIRT1"))
                .body("price", equalTo(150.0F))
                .body("quantity", equalTo(10));

        // 4. Cleanup
        given()
                .when()
                .delete("/api/v1/products/{id}", id)
                .then()
                .statusCode(204);
    }
}
