package com.kcbgroup.learning.jugtours.service;

import com.kcbgroup.learning.jugtours.models.Group;
import com.kcbgroup.learning.jugtours.models.User;
import com.kcbgroup.learning.jugtours.repository.GroupRepository;

import com.kcbgroup.learning.jugtours.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GroupService {
    //    CRUD
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;


    public Group createGroup(Group group, OAuth2User principal)
            throws URISyntaxException {
        Map<String, Object> details = principal.getAttributes();
        String userId = details.get("sub").toString();

        //check to see if user already exists
        Optional<User> user = userRepository.findById(userId);
        group.setUser(user.orElse(new User(userId, details.get("name").toString(), details.get("email").toString())));

        Group result = groupRepository.save(group);
        return result;
    }

    public Group getGroupById(Long groupId) {

        return groupRepository.findById(groupId).get();
    }

    public List<Group> listAllGroups(Principal principal) {
        return groupRepository.findAllByUserId(principal.getName());
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
