package com.runescape.demo.api.model.dao;

import com.runescape.demo.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;
import com.runescape.demo.model.WebOrder;

import java.util.List;

public interface WebOrderDAO extends ListCrudRepository<WebOrder, Long> {
    List<WebOrder> findByUser(LocalUser user);

}
