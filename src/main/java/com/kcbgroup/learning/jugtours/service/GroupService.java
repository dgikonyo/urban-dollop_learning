package com.kcbgroup.learning.jugtours.service;

import com.kcbgroup.learning.jugtours.models.Group;
import com.kcbgroup.learning.jugtours.repository.GroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    //    CRUD
    @Autowired
    GroupRepository groupRepository;

    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    public Group getGroupById(Long groupId) {

        return groupRepository.findById(groupId).get();
    }

    public List<Group> listAllGroups() {
        List<Group> groups = new ArrayList<Group>();
        groupRepository.findAll().forEach(group -> groups.add(group));

        return groups;
    }

    public Group updateGroup(@PathVariable("id") Long groupId, @RequestBody Group group) {
        Optional<Group> groupData = groupRepository.findById(groupId);

        if (groupData.isPresent()) {
            Group _group = groupData.get();
            _group.setName(group.getName());
            _group.setCity(group.getCity());
            _group.setCountry(group.getCountry());
            _group.setStateOrProvince(group.getStateOrProvince());
            _group.setPostalCode(group.getPostalCode());
            _group.setEvents(group.getEvents());
            _group.setAddress(group.getAddress());

            return groupRepository.save(_group);
        }

        return null;
    }

    public Group deleteGroup(Long groupId) {
        Optional<Group> groupData = groupRepository.findById(groupId);

        if (groupData.isPresent()) {
            Group _group = groupData.get();
            _group.setDeleted(true);

            return groupRepository.save(_group);
        }

        return null;
    }
}
