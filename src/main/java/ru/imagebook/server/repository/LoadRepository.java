package ru.imagebook.server.repository;

import java.util.Collection;
import java.util.List;

public interface LoadRepository {
	List<String> loadExistingNumbers(Collection<String> numbers);
}