package org.sundo.wamis.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.sundo.wamis.entities.*;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.StatisticRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final WamisApiService apiService;
    private final WaterLevelFlowRepository waterLevelFlowRepository; // 수위 + 유량 데이터
    private final PrecipitationRepository precipitationRepository; // 강수량 데이터
    private final StatisticRepository statisticRepository; // 통계

    /**
     * 현재 시간 기준 통계 업데이트
     * - 1시간마다 업데이트
     */
    public void update(LocalDate date) {
        date = Objects.requireNonNullElse(date, LocalDate.now());

        String[] types = {"rf", "wl", "flw"};
        QPrecipitation precipitation = QPrecipitation.precipitation;
        QWaterLevelFlow waterLevelFlow = QWaterLevelFlow.waterLevelFlow;

        List<Statistic> sItems = new ArrayList<>();
        for (String type : types) {
            List<Observatory> oList = apiService.getObservatories(type);

            for (Observatory o : oList) {
                String obscd = o.getObscd();
                List<double[]> data = null;
                BooleanBuilder builder = new BooleanBuilder();

                if(type.equals("rf")) { //강수량
                    builder.and(precipitation.rfobscd.eq(obscd))
                            .and(precipitation.ymd.eq(date));

                    List<Precipitation> items = (List<Precipitation>)precipitationRepository.findAll(builder, Sort.by(desc("ymdhm")));

                    data = getRfData(items);

                } else if (type.equals("wl") || type.equals("flw")) {
                    builder.and(waterLevelFlow.wlobscd.eq(obscd))
                            .and(waterLevelFlow.ymd.eq(date));

                    List<WaterLevelFlow> items = (List<WaterLevelFlow>) waterLevelFlowRepository.findAll(builder, Sort.by(desc("ymdhm")));

                    data = getWlfData(items, type);
                }
                StatisticId id = new StatisticId(obscd, type, date);

                Statistic statistic = statisticRepository.findById(id).orElseGet(Statistic::new);
                double[] avg = data.get(0);
                double[] acc = data.get(1);

                statistic.setObscd(obscd);
                statistic.setType(type);
                statistic.setDate(date);

                statistic.setAvg0(avg[0]);
                statistic.setAcc0(acc[0]);

                statistic.setAvg1(avg[1]);
                statistic.setAcc1(acc[1]);

                statistic.setAvg2(avg[2]);
                statistic.setAcc2(acc[2]);

                statistic.setAvg3(avg[3]);
                statistic.setAcc3(acc[3]);

                statistic.setAvg4(avg[4]);
                statistic.setAcc4(acc[4]);

                statistic.setAvg5(avg[5]);
                statistic.setAcc5(acc[5]);

                statistic.setAvg6(avg[6]);
                statistic.setAcc6(acc[6]);

                statistic.setAvg7(avg[7]);
                statistic.setAcc7(acc[7]);

                statistic.setAvg8(avg[8]);
                statistic.setAcc8(acc[8]);

                statistic.setAvg9(avg[9]);
                statistic.setAcc9(acc[9]);

                statistic.setAvg10(avg[10]);
                statistic.setAcc10(acc[10]);

                statistic.setAvg11(avg[11]);
                statistic.setAcc11(acc[11]);

                statistic.setAvg12(avg[12]);
                statistic.setAcc12(acc[12]);

                statistic.setAvg13(avg[13]);
                statistic.setAcc13(acc[13]);

                statistic.setAvg14(avg[14]);
                statistic.setAcc14(acc[14]);

                statistic.setAvg15(avg[15]);
                statistic.setAcc15(acc[15]);

                statistic.setAvg16(avg[16]);
                statistic.setAcc16(acc[16]);

                statistic.setAvg17(avg[17]);
                statistic.setAcc17(acc[17]);

                statistic.setAvg18(avg[18]);
                statistic.setAcc18(acc[18]);

                statistic.setAvg19(avg[19]);
                statistic.setAcc19(acc[19]);

                statistic.setAvg20(avg[20]);
                statistic.setAcc20(acc[20]);

                statistic.setAvg21(avg[21]);
                statistic.setAcc21(acc[21]);

                statistic.setAvg22(avg[22]);
                statistic.setAcc22(acc[22]);

                statistic.setAvg23(avg[23]);
                statistic.setAcc23(acc[23]);

                sItems.add(statistic);
            }
        }
        statisticRepository.saveAllAndFlush(sItems);
    }

    /**
     * 현재 날짜 기준으로 업데이트
     */
    public void update() {
        update(null);
    }


    /**
     * 강수량
     * @param items
     * @return
     */
    private List<double[]> getRfData(List<Precipitation> items) {
        double[] avg = new double[24];
        double[] acc = new double[24];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        if (items != null) {
            Map<Integer, List<Double>> tmp = new HashMap<>();
            items.forEach(item -> {
                LocalTime time = LocalTime.parse(item.getYmdhm(), formatter);
                int hour = time.getHour();
                List<Double> prevData = tmp.getOrDefault(hour, new ArrayList<>());
                prevData.add(item.getRf());

                avg[hour] = prevData.stream().mapToDouble(Double::valueOf).average()
                        .getAsDouble();
                acc[hour] = prevData.stream().mapToDouble(Double::valueOf).sum();
            });
        }
        return Arrays.asList(avg, acc);
    }

    /**
     * 유량, 수위
     * @param items
     * @return
     */
    private List<double[]> getWlfData(List<WaterLevelFlow> items, String type) {
        double[] avg = new double[24];
        double[] acc = new double[24];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        if (items != null) {
            Map<Integer, List<Double>> tmp = new HashMap<>();
            items.forEach(item -> {
                LocalTime time = LocalTime.parse(item.getYmdhm(), formatter);
                int hour = time.getHour();
                List<Double> prevData = tmp.getOrDefault(hour, new ArrayList<>());
                double v = type.equals("wl") ? item.getWl() : item.getFw();
                prevData.add(v);

                avg[hour] = prevData.stream().mapToDouble(Double::valueOf).average()
                        .getAsDouble();
                acc[hour] = prevData.stream().mapToDouble(Double::valueOf).sum();
            });
        }
        return Arrays.asList(avg, acc);
    }
}
