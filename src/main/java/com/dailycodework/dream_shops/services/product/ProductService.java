package com.dailycodework.dream_shops.services.product;

import com.dailycodework.dream_shops.dto.ImageDto;
import com.dailycodework.dream_shops.dto.ProductDto;
import com.dailycodework.dream_shops.exceptions.ProductNotFoundException;
import com.dailycodework.dream_shops.models.Category;
import com.dailycodework.dream_shops.models.Image;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.repositories.CategoryRepository;
import com.dailycodework.dream_shops.repositories.ImageRepository;
import com.dailycodework.dream_shops.repositories.ProductRepository;
import com.dailycodework.dream_shops.requests.AddProductRequest;
import com.dailycodework.dream_shops.requests.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    //le role de ModelMapper est de convertie un objet de type x en un objet de type xDto
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        //1. Recherche ou création de la catégorie
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    //(pour category)
                     return categoryRepository.save(newCategory);
                });
        //2. Affectation de la catégorie à la requête (pour product)
        request.setCategory(category);
        //4. Méthode de Sauvegarde du Produit
        return productRepository.save(createProduct(request));
    }

    //3. Méthode de Création de Produit (et qui va etre afficher dans postman)
    public Product createProduct(AddProductRequest request) {
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            request.getCategory()
        );
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product getProductByName(String name) {
        Product product= productRepository.findProductByName(name);
        if(product==null)
            throw new ProductNotFoundException("Product not found with the name: "+name);
        return product;
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        List<Product> products=  productRepository.findProductByBrand(brand);
        if(products.isEmpty()){
            throw new ProductNotFoundException("Product not found with the brand: "+brand);
        }
        return products;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        List<Product> products= productRepository.findProductByCategoryName(category);
        if(products.isEmpty()){
            throw new ProductNotFoundException("Product not found with the category: "+category);
        }
        return products;
    }

    @Override
    public List<Product> getProductByBrandAndName(String Brand, String Name) {
        List<Product> products= productRepository.findProductByBrandAndName(Brand, Name);
        if(products.isEmpty()){
            throw new ProductNotFoundException("Product not found with the name: "+Name +" and Brand: "+Brand);
        }
        return products;
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        List<Product> products= productRepository.findProductByCategoryNameAndBrand(category, brand);
        if(products.isEmpty()){
            throw new ProductNotFoundException("Product not found with the category: "+category+" and Brand: "+brand);
        }
        return products;
    }


    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product not found!");}
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest product, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,product))
                .map(productRepository :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public Long countProductsByBrand(String brand) {
        long counter= productRepository.countByBrand(brand);
        if (counter==0)
            throw new ProductNotFoundException("Product not found with the brand: "+brand);
        return counter;
    }


    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return  existingProduct;

    }

    @Override
    public ProductDto convertToDto(Product product) {
        //Mappage entre objet product et objet productDTO
        //on utilise ModelMapper pour convertir l'objet product de type Product en un nouvel objet productDto de type ProductDto
        //Syntaxe Générale de ModelMapper
        //T targetObject = modelMapper.map(sourceObject, TargetClass.class);
        //Correspondance directe : Pour que le mappage automatique fonctionne correctement, il est conseillé que les noms des attributs dans les deux classes soient identiques
        ProductDto productDto= modelMapper.map(product, ProductDto.class);
        List<Image> images= imageRepository.findByProductId(product.getId());
        //stream().map(...) est très utile pour transformer les éléments d'une collection en appliquant une fonction de transformation
        List<ImageDto> imageDtos= images.stream().map(image -> modelMapper.map(image,ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }


}
