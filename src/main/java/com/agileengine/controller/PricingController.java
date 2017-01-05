package com.agileengine.controller;

import com.agileengine.dto.PriceDTO;
import com.agileengine.dto.ProductDTO;
import com.agileengine.dto.ProductPriceDTO;
import com.agileengine.dto.TimestampDTO;
import com.agileengine.service.PricingService;
import com.agileengine.transformer.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class PricingController {

    private PricingService pricingService;
    private ProductTransformer productTransformer;

    @Autowired
    public PricingController( PricingService pricingService, ProductTransformer productTransformer ) {
        this.pricingService = pricingService;
        this.productTransformer = productTransformer;
    }

    @RequestMapping( path = "/", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createProduct(@RequestBody ProductPriceDTO productPriceDTO) {
        try {
            System.out.println(productPriceDTO);
            pricingService.saveNewProduct(productTransformer.createProductFromDTO(productPriceDTO));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        }  catch ( Exception e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productTransformer.createProductDTOList(pricingService.getAllProducts()));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productTransformer.createProductDTOFromProduct(pricingService.getProduct(id)));
    }

    @RequestMapping(path = "/{id}/pricing", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getPricing( @PathVariable Long id ) {
        return ResponseEntity.ok(
                productTransformer.createPriceDTOListFromProduct(
                        pricingService.getProductWithPricing(id)
                )
        );
    }

    @RequestMapping(path = "/{productId}/pricing", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> createNewPricing(@PathVariable Long productId, @RequestBody PriceDTO priceDTO) {
        try {
            pricingService.createNewPricing(productId, productTransformer.createPricingFromPriceDTO(priceDTO));
        } catch ( IllegalArgumentException iae ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch ( Exception e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(path = "/{productId}", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        try {
            pricingService.updateProduct(productId, productTransformer.createProductFromProductDTO(productDTO));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RequestMapping(path = "/{productId}/pricing/{pricingId}", method = RequestMethod.PATCH, consumes = "application/json")
    public ResponseEntity<?> updatePricing(
            @PathVariable
            Long productId,
            @PathVariable
            Long pricingId,
            @RequestBody
            PriceDTO priceDTO
    ) {
        try {
            pricingService.updatePricing(productId, pricingId, productTransformer.createPricingFromPriceDTO(priceDTO));
        } catch ( IllegalArgumentException iae ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch ( Exception e ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @RequestMapping(path = "/{productId}/reports/pricehistory", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getPriceHistory(@PathVariable Long productId) {
        List<PriceDTO> priceDTOList = productTransformer.createPriceDTOListFromPricingList(
                pricingService.getPricingHistory(productId)
        );

        return ResponseEntity.ok(priceDTOList);
    }

    @RequestMapping(path = "/reports/bytimestamp", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> getPricesByTimestamp(@RequestBody TimestampDTO timestampDTO) {
        List<PriceDTO> priceDTOList = productTransformer.createPriceDTOListFromPricingList(
                pricingService.getPricingByTimestamp(
                        productTransformer.createLocalDateTimeFromTimestampDTO(timestampDTO)
                )
        );
        return ResponseEntity.ok(priceDTOList);
    }
}
