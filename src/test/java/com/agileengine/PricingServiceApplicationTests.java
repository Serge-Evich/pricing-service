package com.agileengine;

import com.agileengine.domain.Pricing;
import com.agileengine.domain.Product;
import com.agileengine.dto.ProductPriceDTO;
import com.agileengine.repository.PricingEntityRepository;
import com.agileengine.repository.ProductEntityRepository;
import com.agileengine.service.PricingService;
import com.agileengine.transformer.ProductTransformer;
import com.agileengine.util.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private String productName;
    private String newProductName;

    @Before
    public void setUp() {
        productName = String.format("some_product_%s", System.currentTimeMillis());
        newProductName = productName + "_1";
    }
    @After
    public void after() {
        deleteProductByName(productName);
        deleteProductByName(newProductName);
    }

    @Test
	public void contextLoads() {
	}


	@Test
    public void productSaveRepositoryTest() {

        Product expectedProduct = new Product();
        expectedProduct.setName(productName);

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
        String timestamp = "2012-09-09T05:20:35+0200";
        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setProductName(productName);
        productPriceDTO.setTimestamp(timestamp);
        productPriceDTO.setPrice(new BigDecimal(10));

        Product product = productTransformer.createProductFromDTO(productPriceDTO);

        assertEquals(product.getName(), productPriceDTO.getProductName());
        assertEquals(product.getPricingList().get(0).getPrice(), productPriceDTO.getPrice());
        assertNotNull(product.getPricingList().get( 0 ).getTimestamp());
    }

    @Test
    public void testPricingService_getPricingHistory() {

        BigDecimal price_1 = new BigDecimal(1);
        BigDecimal price_2 = new BigDecimal(2);
        BigDecimal price_3 = new BigDecimal(3);

        LocalDateTime localDateTime_1 = LocalDateTime.now();

        String timeZone_1 = "+0100";
        String timeZone_2 = "+0200";
        String timeZone_3 = "+0300";

        Product product = new Product();
        product.setName(productName);

        Pricing pricing_1 = new Pricing();
        pricing_1.setProduct(product);
        pricing_1.setPrice(price_1);
        pricing_1.setTimestamp(localDateTime_1);
        pricing_1.setTimeZone(timeZone_1);

        LocalDateTime localDateTime_2 = localDateTime_1.plusMinutes(1);

        Pricing pricing_2 = new Pricing();
        pricing_2.setProduct(product);
        pricing_2.setPrice(price_2);
        pricing_2.setTimestamp(localDateTime_2);
        pricing_2.setTimeZone(timeZone_2);

        LocalDateTime localDateTime_3 = localDateTime_2.plusMinutes(1);

        Pricing pricing_3 = new Pricing();
        pricing_3.setProduct(product);
        pricing_3.setPrice(price_3);
        pricing_3.setTimestamp(localDateTime_3);
        pricing_3.setTimeZone(timeZone_3);

        product.setPricingList(Arrays.asList(pricing_1, pricing_3, pricing_2));

        productEntityRepository.save(product);

        List<Pricing> pricingList = pricingService.getPricingHistory(product.getId());

        assertEquals(3, pricingList.size());

        assertEquals(timeZone_1, pricingList.get(0).getTimeZone());
        assertEquals(timeZone_2, pricingList.get(1).getTimeZone());
        assertEquals(timeZone_3, pricingList.get(2).getTimeZone());
    }

    @Test
    public void testPricingService_updatePricing() {

        Product product = new Product();

        product.setName(productName);

        BigDecimal price_1 = new BigDecimal(1);
        String timeZone_1 = "+0100";
        String timeZone_2 = "+0200";
        LocalDateTime localDateTime_1 = LocalDateTime.now();

        Pricing pricing_1 = new Pricing();
        pricing_1.setProduct(product);
        pricing_1.setPrice(price_1);
        pricing_1.setTimestamp(localDateTime_1);
        pricing_1.setTimeZone(timeZone_1);

        product.setPricingList(Arrays.asList(pricing_1));

        productEntityRepository.save(product);

        Pricing updatePricing_timeZone = new Pricing();
        updatePricing_timeZone.setTimeZone(timeZone_2);

        pricingService.updatePricing( product.getId(), pricing_1.getId(), updatePricing_timeZone );

        Pricing pricingFromDb = pricingEntityRepository.findOne(pricing_1.getId());

        assertEquals(pricingFromDb.getTimeZone(), updatePricing_timeZone.getTimeZone());
        assertNotNull(pricingFromDb.getTimestamp());
        assertEquals(pricingFromDb.getPrice(), price_1);
        assertEquals(pricingFromDb.getProduct().getId(), product.getId());
    }

    @Test
    public void testPricingService_updateProduct() {

        Product product = new Product();

        product.setName(productName);

        productEntityRepository.save(product);

        assertEquals(productName, productEntityRepository.findOne(product.getId()).getName());

        Product updateProduct = new Product();
        updateProduct.setName(newProductName);

        pricingService.updateProduct(product.getId(), updateProduct );

        assertEquals(newProductName, productEntityRepository.findOne(product.getId()).getName());
    }

    @Test
    public void testPricingService_getPricingListByTimestamp() {

        String queryDate = "2007-09-07T10:10:10";

        Product product = new Product();

        product.setName(productName);

        BigDecimal price_1 = new BigDecimal(1);
        BigDecimal price_2 = new BigDecimal(2);
        BigDecimal price_3 = new BigDecimal(3);

        String timeZone_1 = "+0100";
        String timeZone_2 = "+0200";
        String timeZone_3 = "+0300";

        LocalDateTime date = LocalDateTime.parse(
                queryDate,
                DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT)
        );

        LocalDateTime localDateTime_1 = date.plusHours(1);

        Pricing pricing_1 = new Pricing();
        pricing_1.setProduct(product);
        pricing_1.setPrice(price_1);
        pricing_1.setTimestamp(localDateTime_1);
        pricing_1.setTimeZone(timeZone_1);


        Pricing pricing_2 = new Pricing();
        pricing_2.setProduct(product);
        pricing_2.setPrice(price_2);
        pricing_2.setTimestamp(localDateTime_1.plusHours(2));
        pricing_2.setTimeZone(timeZone_2);

        Pricing pricing_3 = new Pricing();
        pricing_3.setProduct(product);
        pricing_3.setPrice(price_3);
        pricing_3.setTimestamp(localDateTime_1.plusHours(3));
        pricing_3.setTimeZone(timeZone_3);

        Pricing pricing_4 = new Pricing();
        pricing_4.setProduct(product);
        pricing_4.setPrice(price_3);
        pricing_4.setTimestamp(LocalDateTime.now());
        pricing_4.setTimeZone(timeZone_3);


        product.setPricingList(Arrays.asList(pricing_1, pricing_2, pricing_3, pricing_4));

        productEntityRepository.save(product);

        List<Pricing> pricingList = pricingService.getPricingByTimestamp( date );

        assertEquals(3, pricingList.size());
    }

    @Transactional
    private void deleteProductByName( String productName ) {
        Optional<Product> product = productEntityRepository.findProductByName(productName);
        if ( product.isPresent() ) {
            productEntityRepository.delete(product.get().getId());
        }
    }
}
