package ru.imagebook.server.api.pickbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.imagebook.server.model.importing.XOrder;
import ru.imagebook.server.service.pickbook.PickbookOrderService;

/**

 * @since 20.11.2014
 */
@Controller
@RequestMapping("/pickbook/orders")
public class PickbookOrderController {

    @Autowired
    private PickbookOrderService pickbookOrderService;

    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void loadOrder(@RequestBody XOrder xOrder) {
        pickbookOrderService.createOrder(xOrder);
    }

    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateOrder(@RequestBody XOrder xOrder) {
        pickbookOrderService.updateOrder(xOrder);
    }

    @RequestMapping(value = "/renderStarted", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void orderRenderStarted(@RequestBody XOrder xOrder) {
        pickbookOrderService.orderRenderStarted(xOrder);
    }

    @RequestMapping(value = "/renderFinished", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void orderRenderFinished(@RequestBody XOrder xOrder) {
        pickbookOrderService.orderRenderFinished(xOrder);
    }
}
