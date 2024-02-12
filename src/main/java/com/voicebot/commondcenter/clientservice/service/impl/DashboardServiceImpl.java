package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.dashboard.*;
import com.voicebot.commondcenter.clientservice.entity.Application;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ServiceLogService serviceLogService;
    @Autowired
    private ApplicationService applicationService;

    private ServiceLogs logs = null;

    @Autowired
    public ApplicationService getApplicationService() {
        return applicationService;
    }

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
    public ServiceLogs getServiceLogs(DashboardSearchRequest dashboardSearchRequest) {
        List<ServiceLog> serviceLogs = serviceLogService.getServiceLogsForMonth(dashboardSearchRequest);
        this.logs = new ServiceLogs();
        this.logs.setSuccess(0);
        this.logs.setFail(0);

        List<Daily> dailies = new ArrayList<>();
        List<Weekly> weeklies = new ArrayList<>();
        List<Monthly> monthlies = new ArrayList<>();



        Long clientId = dashboardSearchRequest.getClientId();
        Map<Long, String> applicationNameMap = new HashMap<>();
        for (Application application : applicationService.findByClint(clientId) ) {
            applicationNameMap.put(application.getId(), application.getName());
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate sevenDaysAgo = currentDate.minusDays(7);
        LocalDate thirtyDaysAgo = currentDate.minusDays(30);
        System.out.println("Current Date is: " +currentDate);

        for (ServiceLog serviceLog : serviceLogs) {

            LocalDateTime logDateTime = LocalDateTime.ofInstant(serviceLog.getLogDate().toInstant(), ZoneOffset.UTC);
            LocalDate logDate = logDateTime.toLocalDate();
            String timeframe;

            String applicationName = applicationNameMap.get(serviceLog.getApplication());


            if (logDate.equals(currentDate)) {
                String hour = String.valueOf(logDateTime.getHour());
                processHour(dailies,hour,applicationName,serviceLog.getServiceName(),serviceLog.getStatus().name());

            }

            if (logDate.isAfter(sevenDaysAgo)) {
                String date = logDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                processWeekly(weeklies,date,applicationName,serviceLog.getServiceName(),serviceLog.getStatus().name());
                System.out.println("In Weekly" +logDate);

            }
            if (logDate.isAfter(thirtyDaysAgo)) {
                String date = logDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                System.out.println("In Monthly" +logDate);
                processMonhly(monthlies,date,applicationName,serviceLog.getServiceName(),serviceLog.getStatus().name());

            }

        }

        if( !dailies.isEmpty()) {
            logs.setDaily(dailies);
        }

        if( !weeklies.isEmpty()) {
            logs.setWeekly(weeklies);
        }
        if( !monthlies.isEmpty()) {
            logs.setMonthly(monthlies);
        }

        return logs;
    }

    private void processMonhly(List<Monthly> monthlies,String... params) {
        if(monthlies!=null) {
            String date = params[0];
            for (Monthly monthly :monthlies){
                if(StringUtils.equalsIgnoreCase(monthly.getDate(),date)){
                    processApplication(monthly.getData(),params);
                    return;
                }
            }
            Monthly monthly = new Monthly();
            monthly.setDate(date);
            monthly.setData(new ArrayList<ApplicationData>());
            processApplication(monthly.getData(),params);
            monthlies.add(monthly);
        }
    }



    private void processWeekly(List<Weekly> weeklies,String... params) {
        if(weeklies!=null) {
            String day = params[0];
            for (Weekly weekly :weeklies){
                if(StringUtils.equalsIgnoreCase(weekly.getDay(),day)){
                    processApplication(weekly.getData(),params);
                    return;
                }
            }

            Weekly weekly = new Weekly();
            weekly.setDay(day);
            weekly.setData(new ArrayList<ApplicationData>());
            processApplication(weekly.getData(),params);
            weeklies.add(weekly);
        }
    }

    private void processHour(List<Daily> dailies,String... params) {
        if(dailies!=null) {
            String hour = params[0];
            for (Daily daily :dailies){
                if(StringUtils.equalsIgnoreCase(daily.getHour(),hour)){
                    processApplication(daily.getData(),params);
                    return;
                }
            }

            Daily daily1 = new Daily();
            daily1.setHour(hour);
            daily1.setData(new ArrayList<ApplicationData>());
            processApplication(daily1.getData(),params);
            dailies.add(daily1);
        }
    }

    private void processApplication(List<ApplicationData> applications, String... params) {
        if(applications!=null) {
            String application  = params[1];
            for (ApplicationData applicationData:applications) {
                if (StringUtils.equalsIgnoreCase(applicationData.getApplication(),application)) {
                    processService(applicationData.getData(),params);
                    return;
                }
            }
            ApplicationData applicationData = new ApplicationData();
            applicationData.setApplication(application);
            applicationData.setData(new ArrayList<ServiceData>());
            processService(applicationData.getData(),params);
            applications.add(applicationData);

        }
    }

    private void processService(List<ServiceData> serviceDatas,String... params) {
        if(serviceDatas!=null) {
            String serviceName = params[2];
            String status = params[3];
            for (ServiceData serviceData:serviceDatas) {
                if(StringUtils.equalsIgnoreCase(serviceData.getService(),serviceName)){
                    Logs logs = serviceData.getLogs();
                     if(StringUtils.equalsIgnoreCase("SUCCESS",status)) {
                         logs.successPlus1();
                         this.logs.successPlus1();
                     }

                    if(StringUtils.equalsIgnoreCase("FAIL",status)) {
                        logs.failPlus1();
                        this.logs.failPlus1();
                    }
                    return;
                }
            }

            ServiceData serviceData = new ServiceData();
            Logs logs  =new Logs();
            if(StringUtils.equalsIgnoreCase("SUCCESS",status)) {
                logs.setSuccess(1);
                this.logs.successPlus1();
            }
            if(StringUtils.equalsIgnoreCase("FAIL",status)) {
                logs.setFail(1);
                this.logs.failPlus1();
            }
            serviceData.setService(serviceName);
            serviceData.setLogs(logs);

            serviceDatas.add(serviceData);
        }
    }


    @Override
    public Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> getDashboardByClientIdAndStatusAndTimeframe(DashboardSearchRequest dashboardSearchRequest) {
        List<ServiceLog> serviceLogs = serviceLogService.getServiceLogsForMonth(dashboardSearchRequest);

        Long clientId = dashboardSearchRequest.getClientId();
        Map<Long, String> applicationNameMap = new HashMap<>();
        for (Application application : applicationService.findByClint(clientId) ) {
            applicationNameMap.put(application.getId(), application.getName());
        }

        Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> timeframeDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> hourDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> weeklyDataMap = new HashMap<>();
        Map<String, Map<String, Map<String, Map<String, Integer>>>> monthlyDataMap = new HashMap<>();

        String key;
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
            String applicationName = applicationNameMap.get(serviceLog.getApplication());

            if ("Daily".equals(timeframe)) {

                timeframeMap = hourDataMap;
                int hour = logDateTime.getHour();
                key = String.valueOf(hour);
                populateMaps(serviceLog, timeframeMap,key, applicationName);

                //key = String.valueOf(logDate.getDayOfMonth());
                key = logDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                timeframeMap = weeklyDataMap;
                populateMaps(serviceLog, timeframeMap,key, applicationName);

                timeframeMap = monthlyDataMap;
                populateMaps(serviceLog, timeframeMap,key, applicationName);

            } else if ("Weekly".equals(timeframe)) {
                //key = String.valueOf(logDate.getDayOfMonth());
                key = logDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                timeframeMap = weeklyDataMap;
                populateMaps(serviceLog, timeframeMap,key, applicationName);

                timeframeMap = monthlyDataMap;
                populateMaps(serviceLog, timeframeMap,key, applicationName);

            } else {
                //key = String.valueOf(logDate.getDayOfMonth());
                key = logDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                timeframeMap = monthlyDataMap;
                populateMaps(serviceLog, timeframeMap,key, applicationName);
            }

        }
        timeframeDataMap.put("Daily", hourDataMap);
        timeframeDataMap.put("Weekly", weeklyDataMap);
        timeframeDataMap.put("Monthly", monthlyDataMap);

        return timeframeDataMap;

    }

    public void populateMaps(ServiceLog serviceLog, Map<String, Map<String, Map<String, Map<String, Integer>>>> timeframeMap,String key, String applicationName) {
        Map<String, Map<String, Map<String, Integer>>> applicationMap = timeframeMap.computeIfAbsent(key, k -> new HashMap<>());
        Map<String, Map<String, Integer>> serviceMap = applicationMap.computeIfAbsent(applicationName, k -> new HashMap<>());
        Map<String, Integer> statusMap = serviceMap.computeIfAbsent(serviceLog.getServiceName(), k -> new HashMap<>());

        // Update the count for the current status
        String status = serviceLog.getStatus().name();
        statusMap.put(status, statusMap.getOrDefault(status, 0) + 1);

    }

}






