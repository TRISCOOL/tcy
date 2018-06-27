package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.ProductMapper;
import tcy.common.mapper.ProductTypeMapper;
import tcy.common.model.Product;
import tcy.common.model.ProductType;
import tcy.common.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public List<Product> listAllProduct(Integer offset, Integer length) {
        return productMapper.listAllProduct(offset,length);
    }

    @Override
    public List<Product> listProductByType(Integer offset, Integer length, Long productTypeId) {
        return productMapper.listProductByType(offset,length,productTypeId);
    }

    @Override
    public List<ProductType> listProductType(Long parentId, Integer level) {
        return productTypeMapper.selectProductTypeByLevel(parentId,level);
    }

    @Override
    public List<Product> listProductBySearch(Integer offset, Integer length, String found) {
        return productMapper.listProductBySearch(offset,length,found);
    }

    @Override
    public List<Product> listProductWithTag(Integer tag) {
        return productMapper.selectProductForTag(tag);
    }
}
