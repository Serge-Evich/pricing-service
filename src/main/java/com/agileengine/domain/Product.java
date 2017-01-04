package com.agileengine.domain;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false )
    private String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Pricing> pricingList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pricing> getPricingList() {
        return Collections.unmodifiableList( pricingList );
    }

    public void setPricingList(List<Pricing> pricingList) {
        if ( pricingList != null ) {
            this.pricingList.clear();
            this.pricingList.addAll(pricingList);
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", pricingList=").append(pricingList);
        sb.append('}');
        return sb.toString();
    }
}
