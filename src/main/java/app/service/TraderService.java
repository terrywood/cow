package app.service;

import app.entity.Trader;

/**
 * Created by terry.wu on 2016/4/15 0015.
 */
public interface TraderService {


    Trader findOne(Long id);
    void trading(String market, Long id, String code, Integer amount, Double price, String type, Boolean fast);
}
