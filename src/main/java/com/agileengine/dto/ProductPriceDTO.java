package com.agileengine.dto;

import com.agileengine.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductPriceDTO implements Serializable {

    private String productName;
    private BigDecimal price;
    @JsonFormat(pattern= Constants.DATETIME_Z_FORMAT)
    private String timestamp;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProductPriceDTO{");
        sb.append("productName='").append(productName).append('\'');
        sb.append(", price=").append(price);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
