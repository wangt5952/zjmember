package com.laf.manager.controller;

import com.laf.manager.dto.Level;
import com.laf.manager.querycondition.level.LevelSaveForAmountCondition;
import com.laf.manager.service.LevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class LevelController {

    @Autowired
    LevelService levelService;

    @GetMapping("/levels")
    public ModelAndView getAllLevel() {
        List<Level> list = levelService.getAllLevelByMall();
        ModelAndView model = new ModelAndView();

        model.getModel().put("levels", list);
        model.setViewName("levels");
        return model;
    }

    @GetMapping("/level")
    public ModelAndView getLevel(@RequestParam(required = false) Integer levelId) {
        ModelAndView model = new ModelAndView();

        if (levelId != null) {
            Level level = levelService.getLevelById(levelId);

            if (level == null) {
                model.setViewName("error");
            } else {
                model.getModel().put("level", level);
                model.setViewName("level");
            }
        } else {
            model.setViewName("level");
        }

        return model;
    }

    @PostMapping("/saveLevel")
    public String saveLevel(LevelSaveForAmountCondition condition, BindingResult errors) {
        log.info("{}", condition);

        if (errors.hasErrors()) {
            return "redirect:error";
        }

        int result = levelService.editLevelForAmount(condition);

        if (result <= 0) {
            return "redirect:error";
        }

        return "redirect:levels";
    }

    @GetMapping("/delLevel")
    public String delLevel(@RequestParam Integer levelId) {
        int result = levelService.deleteLevelById(levelId);

        if (result <= 0) {
            return "redirect:error";

        } else {
            return "redirect:levels";
        }
    }
}
