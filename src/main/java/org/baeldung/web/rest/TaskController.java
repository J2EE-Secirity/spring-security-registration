package org.baeldung.web.rest;

import org.baeldung.service.TaskServiceImpl;
import org.baeldung.web.dto.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @see https://stackoverflow.com/questions/24642508/spring-inserting-cookies-in-a-rest-call-response/24642844
 */

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskServiceImpl service;

    @RequestMapping(method = RequestMethod.GET)
    public List<TaskDTO> findAll(@RequestParam(required = false) boolean noCache) {
        return service.findAll(noCache);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public List<TaskDTO> test(@RequestParam(required = false) boolean noCache) {
        return service.test(noCache);
    }

    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    public void clearCache() {
        service.clearCache();
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
//    public TaskDTO findById(@RequestParam(required = false) long id) {
    public TaskDTO findById(@RequestParam(required = false) String id) {
//        return service.findById(id);
//        return service.findById(id, new TaskDTO(id, "My first task " + id, true));
        return service.findById(id, new TaskDTO(Long.valueOf(id), "My first task " + id, true));
    }

    @RequestMapping(value = "/find2", method = RequestMethod.GET)
//    public TaskDTO find2ById(@RequestParam(required = false) long id) {
    public TaskDTO find2ById(@RequestParam(required = false) String id) {
//        return service.findById(id, null);
        return service.findById(id, null);
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<String> authenticationStep1(HttpServletResponse response) {

        logger.debug("-------------------------------------- authentication Step #1");
        Cookie cookie = new Cookie("cookieName", "getSignedJWT");
        cookie.setDomain("cookieDomain");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10);
        response.addCookie(cookie);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie","key="+"value");

        return new ResponseEntity<>("OK!", headers, HttpStatus.OK);
    }
}
