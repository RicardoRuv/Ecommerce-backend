package com.runescape.demo.api.controller.order;

import com.runescape.demo.model.LocalUser;
import com.runescape.demo.model.WebOrder;
import com.runescape.demo.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    public OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrdersForUser(@AuthenticationPrincipal LocalUser user) {
        return orderService.getOrdersForUser(user);
    }
}
