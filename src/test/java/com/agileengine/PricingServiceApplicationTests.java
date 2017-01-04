package com.agileengine;

import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import com.agileengine.dto.ProductPriceDTO;
import com.agileengine.repository.PricingEntityRepository;
import com.agileengine.repository.ProductEntityRepository;
import com.agileengine.service.PricingService;
import com.agileengine.transformer.ProductTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PricingServiceApplicationTests {

    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private PricingEntityRepository pricingEntityRepository;
    @Autowired
    private PricingService pricingService;
    @Autowired
    private ProductTransformer productTransformer;


    @Test
	public void contextLoads() {
	}


	@Test
    public void productSaveRepositoryTest() {

        Product expectedProduct = new Product();
        expectedProduct.setName("some_product_" + System.currentTimeMillis());

        Pricing expectedPricing = new Pricing();
        expectedPricing.setPrice(new BigDecimal( 100 ));

        LocalDateTime expectedDate = LocalDateTime.now();

        expectedPricing.setTimestamp( expectedDate );
        expectedPricing.setTimeZone("+0700");
        expectedPricing.setProduct(expectedProduct);

        expectedProduct.setPricingList(Arrays.asList(expectedPricing));

        Product product = productEntityRepository.save(expectedProduct);

        assertEquals( product.getName(), expectedProduct.getName() );

        List<Pricing> pricingEntities = pricingEntityRepository.findByProduct(product);

        assertEquals(
                pricingEntities.get( 0 ).getPrice(),
                expectedPricing.getPrice()
        );
        assertNotNull(
                pricingEntities.get( 0 ).getTimestamp()
        );
    }

    @Test
    public void testPricingService_saveNewProduct() {

    }

    @Test
    public void testProductTransformer() throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        String timestamp = "2012-09-09T05:20:35+0200";
        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductName("Product");
        productPriceDTO.setTimestamp(timestamp);
        productPriceDTO.setPrice(new BigDecimal(10));

        Product product = productTransformer.createProductFromDTO(productPriceDTO);

        assertEquals(product.getName(), productPriceDTO.getProductName());
        assertEquals(product.getPricingList().get(0).getPrice(), productPriceDTO.getPrice());
        assertNotNull(product.getPricingList().get( 0 ).getTimestamp());
    }
}
