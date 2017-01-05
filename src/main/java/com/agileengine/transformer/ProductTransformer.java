package com.agileengine.transformer;


import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import com.agileengine.dto.PriceDTO;
import com.agileengine.dto.ProductDTO;
import com.agileengine.dto.ProductPriceDTO;
import com.agileengine.dto.TimestampDTO;
import com.agileengine.exception.UserException;
import com.agileengine.util.Constants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
public class ProductTransformer {

    public Product createProductFromDTO(ProductPriceDTO productPriceDTO) {
          return createProductFromDTO(productPriceDTO, null);
    }

    public ProductDTO createProductDTOFromProduct( Product product ) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        return productDTO;
    }

    public List<ProductDTO> createProductDTOList( List<Product> productList ) {
        return productList.stream()
                .map( product -> {return createProductDTOFromProduct(product);} )
                .collect(Collectors.toList());
    }

    public Product createProductFromDTO(ProductPriceDTO productPriceDTO, Long id) {

        Product product = new Product();
        product.setId(id);
        product.setName(productPriceDTO.getProductName());

        Pricing pricing = new Pricing();
        pricing.setProduct(product);
        LocalDateTime dateTime = LocalDateTime.parse(getDateTimeFromDate(productPriceDTO.getTimestamp()));
        pricing.setTimestamp(dateTime);
        pricing.setPrice(productPriceDTO.getPrice());
        pricing.setTimeZone(getTimeZoneFromDate(productPriceDTO.getTimestamp()));

        product.setPricingList(Arrays.asList(pricing));
        System.out.println(product);

        return product;
    }

    public List<PriceDTO> createPriceDTOListFromProduct( Product product ) {
        return product.getPricingList().stream()
                .map( pricing -> {
                    return createPriceDTOFromPricing(pricing);
                }).collect(Collectors.toList());

    }

    public Product createProductFromProductDTO( ProductDTO productDTO ) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        return product;
    }

    public Pricing createPricingFromPriceDTO( PriceDTO priceDTO ) {

        Pricing pricing = new Pricing();
        pricing.setPrice(priceDTO.getPrice());
        LocalDateTime localDateTime = LocalDateTime.parse(getDateTimeFromDate(priceDTO.getTimestamp()));
        pricing.setTimestamp(localDateTime);
        pricing.setTimeZone(getTimeZoneFromDate(priceDTO.getTimestamp()));

        return pricing;
    }

    public PriceDTO createPriceDTOFromPricing( Pricing pricing ) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setPrice(pricing.getPrice());
        priceDTO.setId(pricing.getId());
        priceDTO.setProduct( createProductDTOFromProduct(pricing.getProduct()) );
        priceDTO.setTimestamp(
                String.format(
                        "%s%s",
                        pricing.getTimestamp().format(
                                DateTimeFormatter.ofPattern(
                                        Constants.DATETIME_FORMAT
                                )
                        ),
                        pricing.getTimeZone()
                )
        );
        return priceDTO;
    }

    public List<PriceDTO> createPriceDTOListFromPricingList( List<Pricing> pricingList ) {
        return pricingList.stream()
                .map( pricing -> {
                    return createPriceDTOFromPricing(pricing);
                }).collect(Collectors.toList());
    }

    public LocalDateTime createLocalDateTimeFromTimestampDTO(TimestampDTO timestampDTO ) {
        LocalDateTime dateTime = LocalDateTime.parse(
                timestampDTO.getDate(),
                DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT)
        );
        return dateTime;
    }

    private String getTimeZoneFromDate( String dateString ) {
        Matcher m = Constants.DATE_TIMEZONE_PATTERN.matcher(dateString);
        if ( !m.matches() ) {
            throw new UserException(Constants.INVALID_DATE_FORMAT);
        }
        String timeZone = m.group(2);
        return timeZone;
    }

    private String getDateTimeFromDate( String dateString ) {
        Matcher m = Constants.DATE_TIMEZONE_PATTERN.matcher(dateString);
        if ( !m.matches() ) {
            throw new UserException(Constants.INVALID_DATE_FORMAT);
        }
        String timeZone = m.group(1);
        return timeZone;
    }
}
