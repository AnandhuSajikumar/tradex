package com.spring.tradex.Controller;

import com.spring.tradex.DTO.TradeRequest;
import com.spring.tradex.Repositories.UserRepository;
import com.spring.tradex.Service.TransactService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
@RequiredArgsConstructor
@RequestMapping("api/v1/trade")
public class TradeController {
    private final TransactService transactService;
    private final UserRepository userRepository;

}
