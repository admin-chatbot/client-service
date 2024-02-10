package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ServiceLogService serviceLogService;

    @Autowired
    public ServiceLogService getServiceLogService() {
        return serviceLogService;
    }

    @Override
    public DashboardDto getDashboard() {
        return null;
    }

    @Override
    public DashboardDto getDashboardByClintId(Long clientId) {
        return null;
    }

    @Override
    public Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> getDashboardByClientIdAndStatusAndTimeframe(DashboardSearchRequest dashboardSearchRequest) {
        List<ServiceLog> serviceLogs = serviceLogService.getServiceLogsForMonth(dashboardSearchRequest);

        Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> timeframeDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> hourDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> weeklyDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> monthlyDataMap = new HashMap<>();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysAgo = currentDate.minusDays(7);
        LocalDate thirtyDaysAgo = currentDate.minusDays(30);
        System.out.println("Current Date is: " +currentDate);

        // Iterate over each ServiceLog record
        for (ServiceLog serviceLog : serviceLogs) {
            LocalDateTime logDateTime = LocalDateTime.ofInstant(serviceLog.getLogDate().toInstant(), ZoneOffset.UTC);
            LocalDate logDate = logDateTime.toLocalDate();
            String timeframe;

           if (logDate.equals(currentDate)) {
                System.out.println("In Daily" +logDate);
                timeframe = "Daily";
            } else if (logDate.isAfter(sevenDaysAgo)) {
               System.out.println("In Weekly" +logDate);
                timeframe = "Weekly";
            } else if (logDate.isAfter(thirtyDaysAgo)) {
               System.out.println("In Monthly" +logDate);
               timeframe = "Monthly";
            } else {
                continue; // Skip if not within last 30 days
            }
            Map<String, Map<String, Map<String, Map<String, Integer>>>> timeframeMap;
            if ("Daily".equals(timeframe)) {
                System.out.println("In Daily Timeframe");
                timeframeMap = hourDataMap;
            } else if ("Weekly".equals(timeframe)) {
                timeframeMap = weeklyDataMap;
            } else {
                timeframeMap = monthlyDataMap;
            }

            String key;
            if ("Daily".equals(timeframe)) {
                int hour = logDateTime.getHour();
                key = String.valueOf(hour);
            } else {
                key = String.valueOf(logDate.getDayOfMonth());
            }

            Map<String, Map<String, Map<String, Integer>>> applicationMap = timeframeMap.computeIfAbsent(key, k -> new HashMap<>());
            Map<String, Map<String, Integer>> serviceMap = applicationMap.computeIfAbsent(String.valueOf(serviceLog.getApplication()), k -> new HashMap<>());
            Map<String, Integer> statusMap = serviceMap.computeIfAbsent(serviceLog.getServiceName(), k -> new HashMap<>());

            // Update the count for the current status
            String status = serviceLog.getStatus().name();
            statusMap.put(status, statusMap.getOrDefault(status, 0) + 1);

            // Populate the timeframeDataMap

        }
        timeframeDataMap.put("Daily", hourDataMap);
        timeframeDataMap.put("Weekly", weeklyDataMap);
        timeframeDataMap.put("Monthly", monthlyDataMap);

        return timeframeDataMap;

    }
}






