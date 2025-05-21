package com.honeystone.config;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import com.honeystone.board.model.type.Skill;
import com.honeystone.common.editor.LevelEditor;
import com.honeystone.common.editor.LocationEditor;
import com.honeystone.common.editor.SkillEditor;

@ControllerAdvice
public class BinderConfig {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Location.class, new LocationEditor());
        binder.registerCustomEditor(Level.class, new LevelEditor());
        binder.registerCustomEditor(Skill.class, new SkillEditor());
    }
}