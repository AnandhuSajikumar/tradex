package com.spring.tradex.Controller;

import com.spring.tradex.DTO.Trade.TradeMapper;
import com.spring.tradex.DTO.Trade.TradeRequest;
import com.spring.tradex.DTO.Trade.TradeResponse;
import com.spring.tradex.Models.User;
import com.spring.tradex.Repositories.UserRepository;
import com.spring.tradex.Service.TransactService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PageRanges;

@RestController
@Getter
@RequiredArgsConstructor
@RequestMapping("api/v1/trade")
public class TradeController {
    private final TransactService transactService;
    private final UserRepository userRepository;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy")
    public TradeResponse buy(
            @RequestBody TradeRequest request,
            @AuthenticationPrincipal User user
    ){
        return transactService.buyStock(
                user.getId(),
                request.getStockSymbol(),
                request.getQuantity()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all-trades")
    public Page<TradeResponse> allTrades(@PageableDefault(size = 5) Pageable pageable) {
        return transactService.getAllTrade(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders")
    public Page<TradeResponse> tradeHistoryById(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthenticationPrincipal User user
            ){
        return transactService.getTradeHistory(user.getId(), pageable);
    }



}
