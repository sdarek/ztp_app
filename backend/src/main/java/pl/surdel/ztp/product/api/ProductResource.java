package pl.surdel.ztp.product.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.surdel.ztp.product.api.dto.ProductDetailsResponse;
import pl.surdel.ztp.product.api.dto.ProductHistoryResponse;
import pl.surdel.ztp.product.api.dto.ProductRequest;
import pl.surdel.ztp.product.application.ProductService;
import pl.surdel.ztp.product.domain.model.Product;
import pl.surdel.ztp.product.domain.model.ProductHistory;

import java.math.BigDecimal;
import java.util.List;

@Path("api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{id}")
    public Product getProductById(@PathParam("id") Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    @GET
    @Path("{id}/details")
    public ProductDetailsResponse getProductDetails(@PathParam("id") Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));

        ProductDetailsResponse response = new ProductDetailsResponse();
        response.name = product.name;
        response.quantity = product.quantity;
        return response;
    }

    @GET
    @Path("/{id}/history")
    public List<ProductHistoryResponse> getProductHistory(@PathParam("id") Long id) {
        productService.getProductById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));

        List<ProductHistory> history = productService.getProductHistory(id);

        return history.stream().map(h -> {
            ProductHistoryResponse dto = new ProductHistoryResponse();
            dto.oldName = h.oldName;
            dto.newName = h.newName;
            dto.oldCategory = h.oldCategory != null ? h.oldCategory.name : null;
            dto.newCategory = h.newCategory != null ? h.newCategory.name : null;
            dto.oldPrice = h.oldPrice;
            dto.newPrice = h.newPrice;
            dto.oldQuantity = h.oldQuantity;
            dto.newQuantity = h.newQuantity;
            dto.productCreatedAt = h.productCreatedAt;
            dto.productUpdatedAt = h.productUpdatedAt;
            return dto;
        }).toList();
    }

    @POST
    public Product createProduct(ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @PUT
    @Path("/{id}")
    public Product updateProduct(@PathParam("id") Long id, ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new NotFoundException("Product not found with id " + id);
        }
        return Response.noContent().build();
    }
}
