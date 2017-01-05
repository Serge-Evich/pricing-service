package com.agileengine.service;

import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import com.agileengine.repository.PricingEntityRepository;
import com.agileengine.repository.ProductEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PricingService {

    private ProductEntityRepository productEntityRepository;
    private PricingEntityRepository pricingEntityRepository;

    @Autowired
    public PricingService(ProductEntityRepository productEntityRepository, PricingEntityRepository pricingEntityRepository) {
        this.productEntityRepository = productEntityRepository;
        this.pricingEntityRepository = pricingEntityRepository;
    }

    @Transactional
    public Product saveNewProduct(Product product ) {
        checkNotNull(product);
        if ( productEntityRepository.findProductByName(product.getName()).isPresent() ) {
            throw new IllegalArgumentException(String.format("Duplicate product name: %s", product.getName()));
        }
        return productEntityRepository.save( product );
    }

    @Transactional
    public List<Product> getAllProducts() {
        return productEntityRepository.findAll();
    }

    @Transactional
    public Product getProduct( Long id ) {
        Product product = productEntityRepository.findOne(id);
        if ( product == null ) {
            throw new IllegalArgumentException();
        }
        return product;
    }

    @Transactional
    public Product getProductWithPricing( Long id ) {
        Product product = getProduct(id);
        List<Pricing> pricing = pricingEntityRepository.findByProduct(product);
        product.setPricingList(pricing);
        return product;
    }

    @Transactional
    public void createNewPricing(Long productId, Pricing pricing) {
        Product product = getProduct(productId);
        pricing.setProduct(product);
        checkNotNull(pricing);
        pricingEntityRepository.save(pricing);
    }

    @Transactional
    public void updateProduct( Long productId, Product product ) {
        if ( product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if ( productEntityRepository.findProductByName(product.getName()).isPresent() ) {
            throw new IllegalArgumentException();
        }
        Product productForUpdate = productEntityRepository.findOne(productId);
        productForUpdate.setName(product.getName().trim());
    }

    @Transactional
    public void updatePricing( Long productId, Long pricingId, Pricing pricing ) {
        Pricing pricingForUpdate = pricingEntityRepository.findOne(pricingId);
        if ( pricingForUpdate == null ) {
            throw new IllegalArgumentException();
        }
        if (!Objects.equals(productId, pricingForUpdate.getProduct().getId()) ) {
            throw new IllegalArgumentException();
        }
        String timeZone = pricing.getTimeZone();
        LocalDateTime timestamp = pricing.getTimestamp();
        BigDecimal price = pricing.getPrice();

        if (timeZone != null) {
            pricingForUpdate.setTimeZone(timeZone);
        }

        if ( timestamp != null ) {
            pricingForUpdate.setTimestamp(timestamp);
        }

        if ( price != null ) {
            pricingForUpdate.setPrice(price);
        }
    }
    @Transactional
    public List<Pricing> getPricingHistory( Long productId ) {
        List<Pricing> pricingList = pricingEntityRepository.findByProductIdOrderByTimestamp(productId);
        if ( pricingList == null ) {
            throw new IllegalArgumentException();
        }
        return pricingList;
    }

    @Transactional
    public List<Pricing> getPricingByTimestamp( LocalDateTime date ) {
        List<Pricing> pricingList = pricingEntityRepository.findByTimestampBetween(
                date,
                LocalDateTime.of(
                        date.getYear(),
                        date.getMonth(),
                        date.getDayOfMonth() + 1,
                        0,
                        0,
                        0)
        );
        return pricingList;
    }

    private void checkNotNull( Product product ) {
        if ( product == null ) {
            throw new IllegalArgumentException();
        }
        if ( product.getName() == null || product.getPricingList() == null || product.getPricingList().isEmpty() ) {
            throw new IllegalArgumentException();
        }

        product.getPricingList().forEach( pricing -> checkNotNull( pricing ) );
    }

    private void checkNotNull( Pricing pricing ) {

        if ( pricing == null ) {
            throw new IllegalArgumentException();
        }

        if ( pricing.getPrice() == null || pricing.getProduct() == null
                || pricing.getTimestamp() == null || pricing.getTimeZone() == null ) {
              throw new IllegalArgumentException();
        }
    }
}
