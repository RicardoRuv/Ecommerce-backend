package com.runescape.demo.service;

import com.runescape.demo.api.model.dao.WebOrderDAO;
import com.runescape.demo.model.LocalUser;
import com.runescape.demo.model.WebOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private WebOrderDAO webOrderDAO;

    public OrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getOrdersForUser(LocalUser user) {
        return webOrderDAO.findByUser(user);
    }
}
