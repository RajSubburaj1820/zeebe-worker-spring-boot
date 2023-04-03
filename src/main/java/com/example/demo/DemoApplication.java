package com.example.demo;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;


import java.time.Instant;

@SpringBootApplication
@EnableZeebeClient
@Slf4j
public class DemoApplication {

    private static final Logger LOG = LogManager.getLogger(DemoApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @JobWorker(type = "createJira", fetchVariables = {"domain"})
    public void handleJobWorker(final ActivatedJob job) {
        String domain = (String)job.getVariablesAsMap().get("domain");
        LOG.info("Completed job successful with result:"+domain);

        try {
            if (domain == "Bankingg") {
               throw new RuntimeException("Wrong domain name");
            }
        } catch (Exception e) {
            jobClient.newFailedCommand(job).errorMessage("Domain name is invalid").send();

        }

    }


    private static void logJob(final ActivatedJob job) {
        log.info(
                "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getProcessInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                job.getVariables());
    }

}
