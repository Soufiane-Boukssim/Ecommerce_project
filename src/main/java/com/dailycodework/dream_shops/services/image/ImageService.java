package com.dailycodework.dream_shops.services.image;

import com.dailycodework.dream_shops.dto.ImageDto;
import com.dailycodework.dream_shops.exceptions.ImageNotFoundException;
import com.dailycodework.dream_shops.models.Image;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.repositories.ImageRepository;
import com.dailycodework.dream_shops.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ImageNotFoundException("Image not found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundException("Image not found with id: " + id);
        });
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        // Vérifiez si le produit existe
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        List<ImageDto> savedImageDto = new ArrayList<>();
        List<String> skippedFiles = new ArrayList<>(); // Liste pour les fichiers non ajoutés

        for (MultipartFile file : files) {
            try {
                String newFileName = file.getOriginalFilename();

                // Vérifiez si le fichier avec le même nom existe déjà
                if (imageRepository.existsByFileName(newFileName)) {
                    skippedFiles.add(newFileName); // Ajoutez le nom du fichier à la liste des fichiers ignorés
                    continue; // Ignorez ce fichier et passez au suivant
                }

                Image image = new Image();
                image.setFileName(newFileName);
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // Vérifiez si des fichiers ont été ignorés et renvoyez-les
        if (!skippedFiles.isEmpty()) {
            throw new RuntimeException("The following files were not added because they already exist: " + String.join(", ", skippedFiles));
        }

        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        // Récupérer l'image par son ID
        Image image = getImageById(imageId);

        // Vérifier si l'image existe
        if (image == null) {
            throw new RuntimeException("Image not found with ID: " + imageId);
        }

        // Récupérer le nouveau nom de fichier
        String newFileName = file.getOriginalFilename();

        try {
            // Vérifier si le fichier avec le même nom existe déjà
            if (imageRepository.existsByFileName(newFileName)) {
                throw new RuntimeException("A file with the name " + newFileName + " already exists.");
            }

            // Définir les propriétés de l'image
            image.setFileName(newFileName);
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            // Enregistrer l'image
            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage());
        }
    }


}
