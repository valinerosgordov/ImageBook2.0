package ru.imagebook.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.imagebook.server.service.PropertyService;
import ru.imagebook.shared.model.Property;

/**
 * Created by zinchenko on 16.09.14.
 */
@Controller
@RequestMapping("/property")
public class PropertyApi {

    @Autowired
    private PropertyService propertyService;

    @RequestMapping("/{key}")
    public @ResponseBody String get(@PathVariable String key) {
        return propertyService.getValue(key);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody void update(@RequestBody Property property) {
        propertyService.save(property);
    }

    public PropertyService getPropertyService() {
        return propertyService;
    }

    public void setPropertyService(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
}
