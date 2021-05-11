package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CatalogController {

    @Autowired
    private CatalogActions catAct;

    @RequestMapping(path="catalog-page", method = RequestMethod.GET)

    public ModelAndView mainCatPage() throws UnitNotFoundException {
        ModelAndView modelAndView = new ModelAndView("catalogPage");
        modelAndView.addObject("catalog", catAct.loadCatalog(1));
        return modelAndView;
    }
}
