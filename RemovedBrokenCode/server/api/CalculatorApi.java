package ru.imagebook.server.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.imagebook.server.model.calc.CalcProduct;
import ru.imagebook.server.model.calc.CalcProductDetail;
import ru.imagebook.server.model.calc.CalcProductPrice;
import ru.imagebook.server.model.calc.CalcProductRequest;
import ru.imagebook.server.model.calc.CalcProductType;
import ru.imagebook.server.service.CalcService;


@Controller
@RequestMapping("/calc")
public class CalculatorApi {
    @Autowired
    private CalcService calcService;

    @RequestMapping(value = "/product/types", method = RequestMethod.GET)
    public @ResponseBody List<CalcProductType> getProductTypes() {
        return calcService.getProductTypes();
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public @ResponseBody List<CalcProduct> getProducts(@RequestParam Integer type) {
        return calcService.getProductsByType(type);
    }

    @RequestMapping(value = "/products/{id}/detail", method = RequestMethod.GET)
    public @ResponseBody CalcProductDetail getProductDetail(@PathVariable int id) {
        return calcService.getProduct(id);
    }

    @RequestMapping(value = "/bonusLevels", method = RequestMethod.GET)
    public @ResponseBody List<Integer> getBonusLevels() {
        return calcService.getBonusLevels();
    }

    @RequestMapping(value = "/price", method = RequestMethod.POST)
    public @ResponseBody CalcProductPrice computeProductPrice(@Validated @RequestBody CalcProductRequest calcProductRequest) {
        return calcService.computeProductPrice(calcProductRequest);
    }
}
