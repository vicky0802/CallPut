package bulltrack.com.optionanalyzer.utility;

import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrategyLegsFilter;
import java.util.List;

public class OptionCalculator {
    public static OptionCalculator getOptionCalculatorInstance() {
        return new OptionCalculator();
    }

    private OptionCalculator() {
    }

    public double recalculateNetDebit(List<StrategyLegsFilter> list) {
        double d = 0.0d;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAction().equalsIgnoreCase(Constants.ACTION_BUY)) {
                double premium = (double) list.get(i).getPremium();
                Double.isNaN(premium);
                d += premium;
            } else if (list.get(i).getAction().equalsIgnoreCase(Constants.ACTION_SELL)) {
                double premium2 = (double) list.get(i).getPremium();
                Double.isNaN(premium2);
                d -= premium2;
            }
        }
        return d;
    }

    public double reCalculateMaxRisk(List<StrategyLegsFilter> list, double d) {
        List<StrategyLegsFilter> list2 = list;
        if (list2 == null || list.size() == 0) {
            return Double.NaN;
        }
        int strategyId = list2.get(0).getStrategyId();
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        double d5 = 0.0d;
        double d6 = 0.0d;
        double d7 = 0.0d;
        for (int i = 0; i < list.size(); i++) {
            StrategyLegsFilter strategyLegsFilter = list2.get(i);
            if (i == 0) {
                d6 = (double) strategyLegsFilter.getStrike();
                strategyLegsFilter.getCallPut();
                d2 = (double) strategyLegsFilter.getPremium();
            } else if (i == 1) {
                d4 = (double) strategyLegsFilter.getStrike();
                strategyLegsFilter.getCallPut();
                d3 = (double) strategyLegsFilter.getPremium();
            } else if (i == 2) {
                strategyLegsFilter.getCallPut();
                d7 = (double) strategyLegsFilter.getStrike();
                d5 = (double) strategyLegsFilter.getPremium();
            } else if (i == 3) {
                strategyLegsFilter.getStrike();
                strategyLegsFilter.getCallPut();
                strategyLegsFilter.getPremium();
            }
        }
        if (strategyId == 101) {
            return ((d2 + d3) - d4) - d5;
        }
        if (!(strategyId == 102 || strategyId == 103)) {
            if (!(strategyId == 104 || strategyId == 105 || strategyId == 106)) {
                if (strategyId == 107) {
                    d6 -= d4;
                } else if (strategyId != 108) {
                    if (strategyId != 109) {
                        if (strategyId == 110) {
                            return (d7 - d4) + d;
                        }
                        if (strategyId != 111) {
                            if (strategyId == 112) {
                                return (d2 + d3) - d4;
                            }
                            if (strategyId == 113) {
                                return (d3 + d4) - d2;
                            }
                            if (strategyId != 114) {
                                if (strategyId != 115) {
                                    if (strategyId != 116) {
                                        if (strategyId != 117) {
                                            if (!(strategyId == 118 || strategyId == 119)) {
                                                if (strategyId != 120) {
                                                    if (strategyId != 121) {
                                                        if (!(strategyId == 122 || strategyId == 123)) {
                                                            return 0.0d;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                return 3.4028234663852886E38d;
                            }
                        }
                    }
                }
                return d6 + d;
            }
            return d;
        }
        return (d4 - d6) + d;
    }

    public double reCalculateMaxGain(List<StrategyLegsFilter> list, double d) {
        List<StrategyLegsFilter> list2 = list;
        if (list2 == null || list.size() == 0) {
            return Double.NaN;
        }
        int strategyId = list2.get(0).getStrategyId();
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        for (int i = 0; i < list.size(); i++) {
            StrategyLegsFilter strategyLegsFilter = list2.get(i);
            if (i == 0) {
                d4 = (double) strategyLegsFilter.getStrike();
            } else if (i == 1) {
                d2 = (double) strategyLegsFilter.getStrike();
            } else if (i == 2) {
                d3 = (double) strategyLegsFilter.getStrike();
            } else if (i == 3) {
                strategyLegsFilter.getStrike();
            }
        }
        if (strategyId == 101) {
            return (d3 - d2) - ((0.0d - d2) - 0.0d);
        }
        if (!(strategyId == 102 || strategyId == 103)) {
            if (!(strategyId == 104 || strategyId == 105)) {
                if (strategyId != 106) {
                    if (!(strategyId == 107 || strategyId == 108)) {
                        if (strategyId == 109) {
                            d4 -= d2;
                        } else if (strategyId == 110) {
                            return d4 - ((d3 - d2) + d);
                        } else {
                            if (!(strategyId == 111 || strategyId == 112)) {
                                if (strategyId == 113) {
                                    return 0.0d;
                                }
                                if (strategyId == 114) {
                                    return 0.0d + (d2 - 0.0d);
                                }
                                if (strategyId != 115) {
                                    if (strategyId != 116) {
                                        if (strategyId != 117) {
                                            if (strategyId == 118 || strategyId == 119) {
                                                return 0.0d;
                                            }
                                            if (strategyId != 120) {
                                                if (strategyId == 121) {
                                                    return 0.0d - (d2 - d4);
                                                }
                                                if (!(strategyId == 122 || strategyId == 123)) {
                                                    return 0.0d;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return d4 - d;
                    }
                }
                return (d2 - d4) - d;
            }
            return 3.4028234663852886E38d;
        }
        return d * -1.0d;
    }

    public double[] reCalculateBreakevenpoints(List<StrategyLegsFilter> list, double d) {
        double d2;
        char c;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        List<StrategyLegsFilter> list2 = list;
        if (list2 == null || list.size() == 0) {
            return new double[]{Double.NaN, Double.NaN};
        }
        int strategyId = list2.get(0).getStrategyId();
        double[] dArr = {Double.NaN, Double.NaN};
        double reCalculateMaxRisk = reCalculateMaxRisk(list, d);
        double reCalculateMaxGain = reCalculateMaxGain(list, d);
        if (reCalculateMaxRisk >= 0.0d && reCalculateMaxGain >= 0.0d) {
            double d9 = 0.0d;
            double d10 = 0.0d;
            double d11 = 0.0d;
            double d12 = 0.0d;
            double d13 = 0.0d;
            double d14 = 0.0d;
            double d15 = 0.0d;
            for (int i = 0; i < list.size(); i++) {
                StrategyLegsFilter strategyLegsFilter = list2.get(i);
                if (i == 0) {
                    d14 = (double) strategyLegsFilter.getStrike();
                    d9 = (double) strategyLegsFilter.getPremium();
                } else if (i == 1) {
                    double strike = (double) strategyLegsFilter.getStrike();
                    d11 = (double) strategyLegsFilter.getPremium();
                    d12 = strike;
                } else if (i == 2) {
                    double strike2 = (double) strategyLegsFilter.getStrike();
                    d10 = (double) strategyLegsFilter.getPremium();
                    d13 = strike2;
                } else if (i == 3) {
                    strategyLegsFilter.getPremium();
                    d15 = (double) strategyLegsFilter.getStrike();
                }
            }
            if (strategyId == 101) {
                d7 = (d9 - d10) + d11;
            } else {
                if (strategyId == 102) {
                    d4 = d12 + d;
                } else {
                    if (strategyId == 103) {
                        d2 = d12 + d;
                        d3 = d13 - d;
                    } else {
                        if (strategyId == 104) {
                            d2 = d14 - d;
                        } else if (strategyId == 105) {
                            d2 = d12 - d;
                        } else if (strategyId == 106 || strategyId == 107) {
                            d3 = d14 + d;
                            c = 0;
                            d2 = 0.0d;
                            dArr[c] = d2;
                            dArr[1] = d3;
                        } else if (strategyId == 108 || strategyId == 109) {
                            d8 = d14 - d;
                            d3 = 0.0d;
                        } else {
                            if (strategyId == 110) {
                                d5 = d14 - ((d13 - d12) + d);
                                d3 = d <= 0.0d ? d13 + d : 0.0d;
                            } else if (strategyId == 111) {
                                d2 = d < 0.0d ? d14 - d : 0.0d;
                                d3 = d13 + (d12 - d14) + d;
                            } else if (strategyId == 112) {
                                d8 = ((d11 + d12) + d9) - d12;
                                d3 = 0.0d;
                            } else if (strategyId == 113 || strategyId == 114) {
                                d7 = d9 - d11;
                            } else {
                                if (strategyId == 115) {
                                    d3 = 0.0d;
                                    if (d >= 0.0d) {
                                        d5 = d14 - d;
                                    } else {
                                        d6 = d12 - d;
                                    }
                                } else {
                                    d3 = 0.0d;
                                    if (strategyId != 116) {
                                        if (strategyId != 117) {
                                            if (strategyId == 118) {
                                                d2 = d14 + d;
                                                d3 = d14 - d;
                                            } else if (strategyId == 119) {
                                                d4 = d14 + d;
                                            } else if (strategyId != 120) {
                                                if (strategyId == 121) {
                                                    double d16 = d12 - d14;
                                                    d2 = d14 + d + d16;
                                                    d3 = (d12 - d) - d16;
                                                } else if (!(strategyId == 122 || strategyId == 123)) {
                                                    d2 = 0.0d;
                                                }
                                            }
                                        }
                                        d2 = d14 + d;
                                        d3 = d15 - d;
                                    } else if (d >= 0.0d) {
                                        d6 = d12 + d;
                                    } else {
                                        d5 = d14 + d;
                                    }
                                }
                                d2 = d6;
                            }
                            d2 = d5;
                        }
                        d3 = d14 + d;
                    }
                    c = 0;
                    dArr[c] = d2;
                    dArr[1] = d3;
                }
                d3 = d12 - d;
                c = 0;
                dArr[c] = d2;
                dArr[1] = d3;
            }
            d8 = d7;
            d3 = 0.0d;
            c = 0;
            dArr[c] = d2;
            dArr[1] = d3;
        }
        return dArr;
    }
}
