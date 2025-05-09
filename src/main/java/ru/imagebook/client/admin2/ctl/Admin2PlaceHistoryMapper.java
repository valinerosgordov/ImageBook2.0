package ru.imagebook.client.admin2.ctl;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({
		CodesImportPlace.Tokenizer.class,
})
public interface Admin2PlaceHistoryMapper extends PlaceHistoryMapper {
}
