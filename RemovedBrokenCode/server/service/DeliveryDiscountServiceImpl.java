package ru.imagebook.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.client.common.service.delivery.DeliveryDiscountService;
import ru.imagebook.server.repository.DeliveryDiscountRepository;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.service.admin.delivery.DeliveryDiscountExistsException;
import ru.minogin.core.server.hibernate.Dehibernate;

import java.util.List;
import java.util.Map;

/**
 * Created by rifat on 19.01.17.
 */
@Service
public class DeliveryDiscountServiceImpl implements DeliveryDiscountService {
    @Autowired
    private DeliveryDiscountRepository deliveryDiscountRepository;

    @Dehibernate
    @Transactional
    @Override
    public Integer addDeliveryDiscount(DeliveryDiscount deliveryDiscount) throws DeliveryDiscountExistsException {
        try {
            return (deliveryDiscountRepository.addDeliveryDiscount(deliveryDiscount));
        }
        catch (DataIntegrityViolationException e) {
            throw new DeliveryDiscountExistsException();
        }
    }

    @Dehibernate
    @Transactional
    @Override
    public void updateDeliveryDiscount(DeliveryDiscount deliveryDiscount) throws DeliveryDiscountExistsException {
        try {
            deliveryDiscountRepository.updateDeliveryDiscount(deliveryDiscount);
        }
        catch (DataIntegrityViolationException e) {
            throw new DeliveryDiscountExistsException();
        }
    }

    @Dehibernate
    @Transactional
    @Override
    public List<DeliveryDiscount> loadDeliveryDiscounts() {
        return deliveryDiscountRepository.loadDeliveryDiscounts();
    }

    @Dehibernate
    @Transactional
    @Override
    public Map<Integer, DeliveryDiscount> getDiscounts(int sum) {
        return deliveryDiscountRepository.getDiscounts(sum);
    }

    @Dehibernate
    @Transactional
    @Override
    public void deleteDeliveryDiscount(Integer id) {
        deliveryDiscountRepository.deleteDeliveryDiscount(id);
    }
}