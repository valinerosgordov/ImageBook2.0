package ru.imagebook.server.service.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.app.service.BillRemoteService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.server.hibernate.Dehibernate;


@Service
public class BillRemoteServiceImpl implements BillRemoteService {
    @Autowired
    private AuthService authService;

    @Autowired
    private OrderService orderService;

    @Dehibernate
    @Transactional
    @Override
    public BaseBean loadBills() {
        int userId = authService.getCurrentUserId();
        List<Bill> bills = orderService.loadBills(userId);
        BaseBean ret = new BaseBean();
        ret.set("bills", bills);
        return ret;
    }

    @Transactional
    @Override
    public void deleteBill(int billId) {
        User user = authService.getCurrentUser();
        orderService.deleteBillSecure(billId, user.getId());
    }
}
