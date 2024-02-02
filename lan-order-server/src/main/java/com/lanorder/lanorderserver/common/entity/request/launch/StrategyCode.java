package com.lanorder.lanorderserver.common.entity.request.launch;

public enum StrategyCode implements Strategy{
    STORE(4),

    TAB(2),

    MARKETING(5);

    private final Integer strategy;

    StrategyCode(Integer strategy){
        this.strategy = strategy;
    }

    @Override
    public Integer getStrategy() {
        return this.strategy;
    }
}