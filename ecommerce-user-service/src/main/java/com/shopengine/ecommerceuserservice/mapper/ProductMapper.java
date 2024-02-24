package com.shopengine.ecommerceuserservice.mapper;

import com.shopengine.ecommerceuserservice.dto.ProductDTO;
import com.shopengine.ecommerceuserservice.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //Convert Product Entity to ProductDTO
    ProductDTO productToProductDTO(Product product);

    //Convert ProductDTO back to Entity
    Product productDTOToProduct(ProductDTO productDTO);

    //Convert a list of product entities to product DTOs
    List<ProductDTO> productsToProductDTOs(List<Product> products);

    //Convert a list of product DTOs to products
    List<Product> productDTOsToProducts(List<ProductDTO> productDTOs);
}
