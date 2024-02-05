package com.kcbgroup.learning.jugtours.controllers;

import com.kcbgroup.learning.jugtours.models.Group;
import com.kcbgroup.learning.jugtours.service.GroupService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupController {
    private final Logger log = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    GroupService groupService = new GroupService();

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups() {
        try {
            log.info("Request to get all groups");
            List<Group> groups = groupService.listAllGroups();

            if (groups == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(groups, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving information: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/group/{id}")
    ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
        try {
            log.info("Request to get group: {}", id);
            Group groupData = groupService.getGroupById(id);

            if (groupData == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(groupData, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving information: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/group")
    ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) {
        try {
            log.info("Request to create group: {}", group);
            Group _group = groupService.createGroup(group);

            return new ResponseEntity<>(_group, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error saving information: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/group/{id}")
    ResponseEntity<Group> updateGroup(@Valid @RequestBody @PathVariable("id") Long id, Group group) {
        try {
            log.info("Request to update group:{}", group);
            Group groupData = groupService.updateGroup(id, group);

            if (groupData != null) {
                return new ResponseEntity<>(groupData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error saving information: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable("id") Long id) {
        try {
            log.info("Request to delete group: {}", id);
            Group groupData = groupService.deleteGroup(id);

            if (groupData != null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting information: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
