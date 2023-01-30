package com.lilium.elasticsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilium.elasticsearch.document.Vehicle;
import com.lilium.elasticsearch.helper.Indices;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VehicleService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final RestHighLevelClient client;

    @Autowired
    public VehicleService(RestHighLevelClient client) {
        this.client = client;
    }

    public Boolean index(Vehicle vehicle) {
        try {
            String vehicleAsString = MAPPER.writeValueAsString(vehicle);

            IndexRequest request = new IndexRequest(Indices.VEHICLE_INDEX);
            request.id(vehicle.getId());
            request.source(vehicleAsString, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            return response != null && response.status().equals(RestStatus.OK);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public Vehicle getById(String vehicleId) {
        try {
            GetResponse documentFields = client.get(
                    new GetRequest(Indices.VEHICLE_INDEX, vehicleId),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

}
