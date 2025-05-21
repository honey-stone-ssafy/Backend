package com.honeystone.common.editor;

import java.beans.PropertyEditorSupport;

import com.honeystone.board.model.type.Level;

public class LevelEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Level.from(text));
    }
}
