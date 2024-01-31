package com.voicebot.commondcenter.clientservice.service.impl;

import com.voicebot.commondcenter.clientservice.dto.DashboardDto;
import com.voicebot.commondcenter.clientservice.dto.DashboardSearchRequest;
import com.voicebot.commondcenter.clientservice.dto.ServiceCountDto;
import com.voicebot.commondcenter.clientservice.entity.ServiceLog;
import com.voicebot.commondcenter.clientservice.entity.ServiceParameter;
import com.voicebot.commondcenter.clientservice.repository.ServiceLogRepository;
import com.voicebot.commondcenter.clientservice.service.SequenceGeneratorService;
import com.voicebot.commondcenter.clientservice.service.ServiceLogService;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ServiceLogServiceImpl implements ServiceLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ServiceLogRepository serviceLogRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @Override
    public ServiceLog save(ServiceLog serviceLog) {
        serviceLog.setId(sequenceGeneratorService.generateSequence(ServiceLog.SEQUENCE_NAME));
        serviceLog.setCreatedTimestamp(new Date(System.currentTimeMillis()));
        serviceLog.setModifiedTimestamp(new Date(System.currentTimeMillis()));
        return serviceLogRepository.save(serviceLog);
    }

    @Override
    public List<ServiceLog> get() {
        return serviceLogRepository.findAll();
    }

    @Override
    public Page<ServiceLog> get(Pageable pageable) {
        return serviceLogRepository.findAll(pageable);
    }

    @Override
    public Long getTotalCount() {
        return null;
    }

    @Override
    public Long getTotalCount(String serviceName) {
        return null;
    }

    @Override
    public Long getTotalByClient(Long id) {
        return null;
    }

    @Override
    public Long getTotalByClient(String clientName) {
        return null;
    }

    @Override
    public Long getTotalByApplication(Long id) {
        return null;
    }


    Logger logger = Logger.getLogger(ServiceLogServiceImpl.class.getName());
    @Override
    public List<ServiceCountDto> getMaximumCountByServiceName() {

        GroupOperation groupByServiceEndpoint = Aggregation.group("serviceName").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"));
        ProjectionOperation project = Aggregation.project("serviceName", "count");
        Aggregation aggregation = Aggregation.newAggregation(groupByServiceEndpoint, sortOperation, project);
        AggregationResults<ServiceCountDto> output = mongoTemplate.aggregate(aggregation, "servicelog", ServiceCountDto.class);

        logger.info("Raw results: {}" + output.getRawResults());
        logger.info("Mapped results: {}" + output.getMappedResults());
        return output.getMappedResults().stream().limit(10).toList();

    }

    public List<ServiceCountDto> getMinimumCountByServiceName() {

        GroupOperation groupByServiceEndpoint = Aggregation.group("serviceName").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.ASC, "count"));
        ProjectionOperation project = Aggregation.project("serviceName", "count");
        Aggregation aggregation = Aggregation.newAggregation(groupByServiceEndpoint, sortOperation, project);
        AggregationResults<ServiceCountDto> output = mongoTemplate.aggregate(aggregation, "servicelog", ServiceCountDto.class);
        logger.info("Raw results: {}" + output.getRawResults());
        logger.info("Mapped results: {}" + output.getMappedResults());
        return output.getMappedResults().stream().limit(10).toList();


    }

    public List<ServiceCountDto> getMaximumCountByServiceNameByClient(Long clintId) {
        GroupOperation groupByServiceEndpoint = Aggregation.group("serviceName").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"));
        ProjectionOperation project = Aggregation.project("serviceName", "count");
        Aggregation aggregation = Aggregation.newAggregation(groupByServiceEndpoint, sortOperation, project);
        AggregationResults<ServiceCountDto> output = mongoTemplate.aggregate(aggregation, "servicelog", ServiceCountDto.class);

        logger.info("Raw results: {}" + output.getRawResults());
        logger.info("Mapped results: {}" + output.getMappedResults());
        return output.getMappedResults().stream().limit(10).toList();
    }

    @Override
    public Long getTotalByApplication(String clientName) {
        return null;
    }

    @Override
    public Long getTotalByClientAndApplication(Long id) {
        return null;
    }

    @Override
    public Long getTotalByClientAndApplication(String clientName) {
        return null;
    }

    @Override
    public List<String> getTopNServices(int n) {
        return null;
    }

    @Override
    public List<ServiceLog> getServiceLogCountByStatusAndDate(DashboardSearchRequest dashboardSearchRequest) {
        Date startDate = null;
        Date endDate = new Date(); // Current date

        String timeRange = dashboardSearchRequest.getTimeFrame();
        Long clientId = dashboardSearchRequest.getClientId();

        // Calculate start date based on time range
        switch (timeRange) {
            case "currentDay":
                startDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            case "lastWeek":
                startDate = Date.from(LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            case "lastMonth":
                startDate = Date.from(LocalDate.now().minusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            default:
                // Default to current day
                startDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
        }

        // Create the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                // Match documents for the specified client and within the specified date range
                Aggregation.match(Criteria.where("client").is(clientId)
                        .and("logDate").gte(startDate).lte(endDate))
                // Group documents by status and count the occurrences
                //Aggregation.group("status").count().as("count")
        );

        // Execute the aggregation query and return the result
        return mongoTemplate.aggregate(aggregation, "servicelog", ServiceLog.class).getMappedResults();
    }
}


