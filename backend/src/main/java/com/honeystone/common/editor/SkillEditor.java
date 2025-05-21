package com.honeystone.common.editor;

import java.beans.PropertyEditorSupport;

import com.honeystone.board.model.type.Skill;

public class SkillEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Skill.from(text));
    }
}
