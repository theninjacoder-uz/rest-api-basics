package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepo<T> {

    Optional<T> create (T t);

    Optional<T> get(Long id);

    List<T> getList();

    boolean delete(Long id);

    Optional<T> update(Long id, T t);
}
