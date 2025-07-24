package com.shiroha.pandarunner.converter;

import com.shiroha.pandarunner.domain.dto.ProductCategoryDTO;
import com.shiroha.pandarunner.domain.dto.ProductDTO;
import com.shiroha.pandarunner.domain.entity.Product;
import com.shiroha.pandarunner.domain.entity.ProductCategory;
import com.shiroha.pandarunner.domain.vo.ProductCategoryVO;
import com.shiroha.pandarunner.domain.vo.ProductVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductConverter {

    ProductCategory categoryDtoToCategory(ProductCategoryDTO productCategoryDTO);

    ProductCategoryVO productCategoryToCategoryVO(ProductCategory productCategory);

    ProductDTO productToProductDto(Product product);

    List<ProductDTO> productListToProductDtoList(List<Product> products);

    ProductVO productToProductVO(Product product);

    List<ProductVO> productListToProductVOList(List<Product> products);

    Product productDtoToProduct(ProductDTO productDTO);

}
