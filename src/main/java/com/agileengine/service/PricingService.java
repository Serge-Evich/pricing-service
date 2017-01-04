package com.agileengine.service;

import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import com.agileengine.repository.PricingEntityRepository;
import com.agileengine.repository.ProductEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
