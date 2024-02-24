package com.shopengine.ecommerceuserservice.service;

import com.shopengine.ecommerceuserservice.dto.PriceResponse;
import com.shopengine.ecommerceuserservice.dto.ProductDTO;
import com.shopengine.ecommerceuserservice.exception.ProductNotFoundException;
import com.shopengine.ecommerceuserservice.mapper.ProductMapper;
import com.shopengine.ecommerceuserservice.model.Product;
import com.shopengine.ecommerceuserservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    //Create a new product
    public ProductDTO saveProduct(ProductDTO productDTO){
        Product product = productMapper.productDTOToProduct(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(savedProduct);
    }

    // Get a product by ID
    public ProductDTO getProductById(Long id) {

        Product product =  productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        return mergeProductData(product);
    }

    // Update a product by ID
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get all products
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mergeProductData).collect(Collectors.toList());
    }

    private ProductDTO mergeProductData(Product product){
        ProductDTO productDTO = productMapper.productToProductDTO(product);
        Double externalPrice = fetchExternalPrice(productDTO.getId());
        productDTO.setPrice(externalPrice != null ? externalPrice : product.getPrice());

        return productDTO;

    }

    private Double fetchExternalPrice(Long productId){
        String url = "https://api.example.com/prices/" + productId;

        try{
            PriceResponse response = restTemplate.getForObject(url, PriceResponse.class);
            return response != null ? response.getPrice() : null;
        }
        catch (Exception e){
            System.err.println("Error fecthing price :" + e.getMessage());
            return  null;
        }
    }


}
