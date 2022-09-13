package dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dao.de.honoka.ProductOrder;
import dao.service.ProductOrderService;
import dao.mapper.ProductOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author Ryu Mun
* @description 针对表【product_order】的数据库操作Service实现
* @createDate 2022-07-25 14:01:29
*/
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder>
    implements ProductOrderService{

}




