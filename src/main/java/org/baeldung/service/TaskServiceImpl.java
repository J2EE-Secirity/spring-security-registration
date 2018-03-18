package org.baeldung.service;

import org.baeldung.web.dto.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @CachePut(value = "tasks", condition = "#noCache", key = "#noCache")
    @Cacheable(value = "tasks", condition = "!#noCache", key = "!#noCache")
    public List<TaskDTO> findAll(boolean noCache) {
        logger.info("> Retrieving tasks...");
        return Arrays.asList(new TaskDTO(1L, "My first task", true), new TaskDTO(2L, "My second task", false));
    }

    @CachePut(value = "tasks", condition = "#noCache", key = "#noCache")
    @Cacheable(value = "tasks", condition = "!#noCache", key = "!#noCache")
    public List<TaskDTO> test(boolean noCache) {
        logger.info(">> test...");
        return new ArrayList<>();
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void clearCache() {
        logger.info("< Cache cleared...");
    }


    @Cacheable(cacheNames = "tasks", key = "#id")
//    public TaskDTO findById(long id) {
//    public TaskDTO findById(long id, TaskDTO taskDTO) {
    public TaskDTO findById(String id, TaskDTO taskDTO) {
        logger.info("> Retrieving tasks " + id + " ...");
//        return new TaskDTO(id, "My first task " + id, true);
        return taskDTO;
    }
}
