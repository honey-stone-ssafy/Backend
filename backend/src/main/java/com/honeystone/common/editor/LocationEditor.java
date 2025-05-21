package com.honeystone.common.editor;

import java.beans.PropertyEditorSupport;
import com.honeystone.board.model.type.Location;

public class LocationEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Location.from(text));
    }
}
