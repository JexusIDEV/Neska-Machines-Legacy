package com.jgeb.neska_machines_l.util.datas;

public enum NeskaMachineTiers {
    BASIC(1f, 1, 1),
    ADVANCED(1.75f, 2, 2),
    COMPLEX(2.5f, 5, 3);

    private final float efficiencyMulti;
    private final int energyConsumerMulti;
    private int tier;

    NeskaMachineTiers(float efficiencyMulti, int energyConsumerMulti, int tier) {
        this.efficiencyMulti = efficiencyMulti;
        this.energyConsumerMulti = energyConsumerMulti;
        this.tier = tier;
    }

    public float getEfficiencyMulti() {
        return this.efficiencyMulti;
    }

    public int getEnergyConsumerMulti() {
        return this.energyConsumerMulti;
    }

    public int getTier() {
        return this.tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
