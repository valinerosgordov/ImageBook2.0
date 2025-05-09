package ru.imagebook.server.service2.weight;

import ru.imagebook.shared.model.AlbumOrder;
import ru.minogin.core.server.file.TempFile;

public interface WeightService {
	int getItemWeight(AlbumOrder order);

	int getTotalWeight(AlbumOrder order);

	TempFile createWeightReport();
}
